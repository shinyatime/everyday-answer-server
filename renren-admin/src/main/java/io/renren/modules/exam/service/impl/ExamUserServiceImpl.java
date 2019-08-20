package io.renren.modules.exam.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.exam.vo.UserQuestionVO;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.exam.dao.ExamUserDao;
import io.renren.modules.exam.entity.ExamUserEntity;
import io.renren.modules.exam.service.ExamUserService;


@Service("examUserService")
public class ExamUserServiceImpl extends ServiceImpl<ExamUserDao, ExamUserEntity> implements ExamUserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExamUserEntity> page = this.page(
                new Query<ExamUserEntity>().getPage(params),
                new QueryWrapper<ExamUserEntity>()
        );

        return new PageUtils(page);
    }


}
