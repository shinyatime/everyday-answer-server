package io.renren.modules.exam.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.exam.dao.ExamIntegralCountDao;
import io.renren.modules.exam.entity.ExamIntegralCountEntity;
import io.renren.modules.exam.service.ExamIntegralCountService;


@Service("examIntegralCountService")
public class ExamIntegralCountServiceImpl extends ServiceImpl<ExamIntegralCountDao, ExamIntegralCountEntity> implements ExamIntegralCountService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExamIntegralCountEntity> page = this.page(
                new Query<ExamIntegralCountEntity>().getPage(params),
                new QueryWrapper<ExamIntegralCountEntity>()
        );

        return new PageUtils(page);
    }

}
