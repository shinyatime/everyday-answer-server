package io.renren.modules.exam.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.exam.dao.ExamQuestionidDao;
import io.renren.modules.exam.entity.ExamQuestionidEntity;
import io.renren.modules.exam.service.ExamQuestionidService;


@Service("examQuestionidService")
public class ExamQuestionidServiceImpl extends ServiceImpl<ExamQuestionidDao, ExamQuestionidEntity> implements ExamQuestionidService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExamQuestionidEntity> page = this.page(
                new Query<ExamQuestionidEntity>().getPage(params),
                new QueryWrapper<ExamQuestionidEntity>()
        );

        return new PageUtils(page);
    }

}
