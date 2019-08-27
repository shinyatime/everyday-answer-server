package io.renren.modules.exam.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.exam.vo.ExamIntegralDetailsVO;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.exam.dao.ExamIntegralDetailsDao;
import io.renren.modules.exam.entity.ExamIntegralDetailsEntity;
import io.renren.modules.exam.service.ExamIntegralDetailsService;


@Service("examIntegralDetailsService")
public class ExamIntegralDetailsServiceImpl extends ServiceImpl<ExamIntegralDetailsDao, ExamIntegralDetailsEntity> implements ExamIntegralDetailsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
//        IPage<ExamIntegralDetailsEntity> page = this.page(
//                new Query<ExamIntegralDetailsEntity>().getPage(params),
//                new QueryWrapper<ExamIntegralDetailsEntity>()
//        );

        IPage<ExamIntegralDetailsVO> page = baseMapper.getIntegralDetailsList(
                new Query<ExamIntegralDetailsVO>().getPage(params,"create_time",false),params
        );
        return new PageUtils(page);
    }

    @Override
    public IPage<ExamIntegralDetailsVO> getIntegralDetailsList(Page page, Map<String, Object> params) {
        return baseMapper.getIntegralDetailsList(page,params);
    }

}
