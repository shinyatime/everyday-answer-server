package io.renren.modules.exam.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.exam.entity.ExamIntegralCountEntity;
import io.renren.modules.exam.entity.ExamIntegralDetailsEntity;
import io.renren.modules.exam.entity.ExamQuestionEntity;
import io.renren.modules.exam.service.ExamIntegralCountService;
import io.renren.modules.exam.service.ExamIntegralDetailsService;
import io.renren.modules.exam.service.ExamQuestionService;
import io.renren.modules.exam.vo.UserQuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.exam.dao.ExamUserQuestionDao;
import io.renren.modules.exam.entity.ExamUserQuestionEntity;
import io.renren.modules.exam.service.ExamUserQuestionService;


@Service("examUserQuestionService")
public class ExamUserQuestionServiceImpl extends ServiceImpl<ExamUserQuestionDao, ExamUserQuestionEntity> implements ExamUserQuestionService {

//    @Autowired
//    private RedisUtils redisUtils;

    @Autowired
    private ExamUserQuestionService examUserQuestionService;

    @Autowired
    private ExamIntegralDetailsService examIntegralDetailsService;

    @Autowired
    private ExamIntegralCountService examIntegralCountService;

    @Autowired
    private ExamQuestionService examQuestionService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExamUserQuestionEntity> page = this.page(
                new Query<ExamUserQuestionEntity>().getPage(params),
                new QueryWrapper<ExamUserQuestionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public boolean saveAnswer(ExamUserQuestionEntity userQuestionEntity) {
        boolean flag = false;
        Date date = new Date();
        String openid = userQuestionEntity.getOpenid();

//        List<Object> ids = redisUtils.getList("question_ids");
//        if(!ids.contains(userQuestionEntity.getQuestionid()+"")) {
//            throw new RRException("题目已经过期！");
//        }

        QueryWrapper<ExamUserQuestionEntity> wrappereuq = new QueryWrapper<>();
        wrappereuq.eq("openid", openid);
        wrappereuq.eq("DATE_FORMAT(create_time,'%Y-%m-%d')", DateUtils.format(date));
        wrappereuq.eq("questionid", userQuestionEntity.getQuestionid());
        if(examUserQuestionService.count() < 1) {
            throw new RRException("题目已经过期！");
        }


        QueryWrapper<ExamIntegralDetailsEntity> wrappereid = new QueryWrapper<>();
        wrappereid.eq("openid", openid);
        wrappereid.eq("DATE_FORMAT(create_time,'%Y-%m-%d')", DateUtils.format(date));
        ExamIntegralDetailsEntity detailsEntity = examIntegralDetailsService.getOne(wrappereid);


        //ExamQuestionEntity questionEntity = (ExamQuestionEntity) redisUtils.getHash("question_list",String.valueOf(userQuestionEntity.getQuestionid()));

        ExamQuestionEntity questionEntity = examQuestionService.getById(userQuestionEntity.getQuestionid());
        if (userQuestionEntity.getAnswer().equals(questionEntity.getAnswer())) {
            //userQuestionEntity.setIntegral(questionEntity.getScore());

            detailsEntity.setRightNum(detailsEntity.getRightNum()+1);
            flag = true;
        }
        userQuestionEntity.setUpdateTime(date);
        userQuestionEntity.setStatus("1");
        QueryWrapper<ExamUserQuestionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        wrapper.eq("questionid", userQuestionEntity.getQuestionid());

        examUserQuestionService.update(userQuestionEntity,wrapper);

        Long count = 5L;

        if (Long.valueOf(userQuestionEntity.getAnswerOrder()) == count) {
            detailsEntity.setIntegral(1);
        }
        detailsEntity.setFinishNum(detailsEntity.getFinishNum()+1);
        examIntegralDetailsService.updateById(detailsEntity);


        if (Long.valueOf(userQuestionEntity.getAnswerOrder()) == count) {
//            ExamIntegralDetailsEntity detailsEntity = new ExamIntegralDetailsEntity();
//            detailsEntity.setUserId(userQuestionEntity.getUserId());
//            detailsEntity.setOpenid(openid);
//            detailsEntity.setContent("每日答题");
//            detailsEntity.setIntegral(1);
//            detailsEntity.setIntegralType("0");
//            detailsEntity.setCreateTime(new Date());
//            examIntegralDetailsService.save(detailsEntity);


            QueryWrapper<ExamIntegralCountEntity> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("user_id", userQuestionEntity.getUserId());
            ExamIntegralCountEntity countEntity = examIntegralCountService.getOne(wrapper1);
            if (countEntity != null) {
                Integer icount = countEntity.getIntegralCount();
                countEntity.setIntegralCount(icount+1);
                countEntity.setUpdateTime(date);
            } else {
                countEntity = new ExamIntegralCountEntity();
                countEntity.setUserId(userQuestionEntity.getUserId());
                countEntity.setIntegralCount(1);
                countEntity.setCreateTime(date);
            }
            examIntegralCountService.saveOrUpdate(countEntity);
            //redisUtils.delete("user_" + openid);
        } else {
            //redisUtils.removeList("user_" + openid, 1L, userQuestionEntity.getQuestionid());
        }
        return flag;
    }

    @Override
    public IPage<UserQuestionVO> getUserQuestion(Page page, Map<String, Object> map) {
        return baseMapper.getUserQuestion(page,map);
    }

    @Override
    public UserQuestionVO getUserQuestionOne(Long id) {
        return baseMapper.getUserQuestionOne(id);
    }

}
