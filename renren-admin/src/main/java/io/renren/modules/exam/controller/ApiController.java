package io.renren.modules.exam.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.exception.RRException;
import io.renren.common.utils.R;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.exam.entity.ExamIntegralDetailsEntity;
import io.renren.modules.exam.entity.ExamQuestionEntity;
import io.renren.modules.exam.entity.ExamUserEntity;
import io.renren.modules.exam.entity.ExamUserQuestionEntity;
import io.renren.modules.exam.service.*;
import io.renren.modules.exam.vo.UserQuestionVO;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ExamUserService examUserService;

    @Autowired
    private ExamQuestionService examQuestionService;

    @Autowired
    private ExamUserQuestionService examUserQuestionService;

    @Autowired
    private ExamIntegralDetailsService examIntegralDetailsService;



    @PostMapping("/user/getOpenid")
    public R getOpenid(@RequestBody JSONObject json) {
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(json.getString("code"));
            String openid = session.getOpenid();

            String userid = String.valueOf(redisUtils.getHash("regUserList", openid));
            if (userid == null|| "null".equals(userid) || "".equals(userid)) {
                QueryWrapper<ExamUserEntity> wrapper = new QueryWrapper<>();
                wrapper.eq("openid", openid);
                ExamUserEntity examUserEntity = examUserService.getOne(wrapper);
                if (examUserEntity == null) {
                    return R.ok().put("openid", openid).put("isreg",false);
                }
                if ("".equals(examUserEntity.getOpenid())) {
                    return R.ok().put("openid", openid).put("isreg",false);
                }
            }
            return R.ok().put("openid", openid).put("isreg",true);
        } catch (WxErrorException e) {
            return R.error(e.getMessage());
        }

    }

    @PostMapping("/user/reg")
    public R reg(@RequestBody ExamUserEntity userEntity) {
        QueryWrapper<ExamUserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", userEntity.getOpenid());
        ExamUserEntity examUserEntity = examUserService.getOne(wrapper);
        if (examUserEntity != null) {
            return R.ok().put("isreg",true);
        }
        userEntity.setCreateTime(new Date());
        examUserService.save(userEntity);
        redisUtils.setHash("regUserList",userEntity.getOpenid()+"",userEntity.getId());
        return R.ok();
    }

    @PostMapping("/user/getUserInfo")
    public R getUser(@RequestBody ExamUserEntity userEntity) {
        String openid = userEntity.getOpenid();
        if(openid==null||"".equals(openid)) {//参数错误
            return R.error("参数格式错误");
        }
        String userid = String.valueOf(redisUtils.getHash("regUserList", openid)) ;

        if ("".equals(userid)) {
            return R.error("用户信息错误");
        }
        userEntity.setId(Long.valueOf(userid));
        boolean flag = redisUtils.isKey("user_"+openid);
        String questionid = "";
        if (flag) {
            questionid = String.valueOf(redisUtils.getListIndex("user_" + openid, 0));
        } else {
            questionid = String.valueOf(examQuestionService.saveUserQuestion(userEntity));
        }
        if ("".equals(questionid)) {
            throw new RRException("今日答题完毕！");
        }
        ExamQuestionEntity questionEntity=null;
        //if (!"".equals(questionid)) {
            questionEntity = (ExamQuestionEntity) redisUtils.getHash("question_list",questionid);
            questionEntity.setAnswer("");
        //}
        Long count = redisUtils.getListSize("question_ids");
        Long num = count - redisUtils.getListSize("user_" + openid) + 1;
        return R.ok().put("question",questionEntity).put("count",count).put("num",num);
    }

    @PostMapping("/user/saveAnswer")
    public R saveAnswer(@RequestBody ExamUserQuestionEntity userQuestionEntity) {
        String openid = userQuestionEntity.getOpenid();
        if(openid==null||"".equals(openid)) {//参数错误
            return R.error("参数格式错误");
        }
        String userid = String.valueOf(redisUtils.getHash("regUserList", openid)) ;

        if ("".equals(userid)) {
            return R.error("用户信息错误");
        }
        userQuestionEntity.setUserId(Long.valueOf(userid));
        boolean flag = examUserQuestionService.saveAnswer(userQuestionEntity);

        return R.ok().put("isRight",flag);
    }


    @PostMapping("/user/getUserQuestionList")
    @ResponseBody
    public R getUserQuestionList(@RequestBody Map<String,Object> map) {
        String openid = map.containsKey("openid")?map.get("openid").toString():"";
        if(openid==null||"".equals(openid)) {//参数错误
            return R.error("参数格式错误");
        }
        String userid = String.valueOf(redisUtils.getHash("regUserList", openid)) ;
        if ("".equals(userid)) {
            return R.error("用户信息错误");
        }
        int current = map.containsKey("page")?Integer.valueOf(map.get("page").toString()):0;
        int sizt = map.containsKey("limit")?Integer.valueOf(map.get("limit").toString()):2;
        QueryWrapper<ExamIntegralDetailsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        wrapper.orderByDesc("create_time");
        IPage<ExamIntegralDetailsEntity> page = examIntegralDetailsService.page(new Page(current,sizt),wrapper);
        return R.ok().put("page",page);
    }

    @PostMapping("/user/getUserQuestion")
    @ResponseBody
    public R getUserQuestionPage(@RequestBody Map<String,Object> map) {
        String openid = map.containsKey("openid")?map.get("openid").toString():"";
        if(openid==null||"".equals(openid)) {//参数错误
            return R.error("参数格式错误");
        }
        String userid = String.valueOf(redisUtils.getHash("regUserList", openid)) ;
        if ("".equals(userid)) {
            return R.error("用户信息错误");
        }
        int current = map.containsKey("page")?Integer.valueOf(map.get("page").toString()):0;
        int sizt = map.containsKey("limit")?Integer.valueOf(map.get("limit").toString()):2;
        IPage<UserQuestionVO> page = examUserQuestionService.getUserQuestion(new Page(current,sizt),map);
        return R.ok().put("page",page);
    }

    @PostMapping("/user/getUserQuestionOne")
    @ResponseBody
    public R getUserQuestionOne(@RequestBody Map<String,Object> map) {
        String openid = map.containsKey("openid")?map.get("openid").toString():"";

        if(openid==null||"".equals(openid)) {//参数错误
            return R.error("参数格式错误");
        }
        String userid = String.valueOf(redisUtils.getHash("regUserList", openid)) ;
        if ("".equals(userid)) {
            return R.error("用户信息错误");
        }
        Long id = map.containsKey("id")?Long.valueOf(map.get("id").toString()):0L;
        if (id == 0L) {
            return R.error("参数错误");
        }
        UserQuestionVO userQuestion = examUserQuestionService.getUserQuestionOne(id);
        return R.ok().put("userQuestion",userQuestion);
    }

    @PostMapping("/user/getUserQuestionOneView")
    @ResponseBody
    public R getUserQuestionOneView(@RequestBody Map<String,Object> map) {
        String openid = map.containsKey("openid")?map.get("openid").toString():"";

        if(openid==null||"".equals(openid)) {//参数错误
            return R.error("参数格式错误");
        }
        String userid = String.valueOf(redisUtils.getHash("regUserList", openid)) ;
        if ("".equals(userid)) {
            return R.error("用户信息错误");
        }
        Long questionid = map.containsKey("questionid")?Long.valueOf(map.get("questionid").toString()):0L;
        if (questionid == 0L) {
            return R.error("参数错误");
        }

        QueryWrapper<ExamUserQuestionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        wrapper.eq("questionid", questionid);
        ExamUserQuestionEntity userQuestionEntity = examUserQuestionService.getOne(wrapper);

        UserQuestionVO userQuestion = examUserQuestionService.getUserQuestionOne(userQuestionEntity.getId());
        return R.ok().put("userQuestion",userQuestion);
    }
}
