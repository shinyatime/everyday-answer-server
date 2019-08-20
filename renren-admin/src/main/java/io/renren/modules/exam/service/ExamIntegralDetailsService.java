package io.renren.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.exam.entity.ExamIntegralDetailsEntity;
import io.renren.modules.exam.vo.ExamIntegralDetailsVO;

import java.util.Map;

/**
 * 
 *
 * @author tinu
 * @email 1810989@qq.com
 * @date 2019-07-14 12:07:49
 */
public interface ExamIntegralDetailsService extends IService<ExamIntegralDetailsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    IPage<ExamIntegralDetailsVO> getIntegralDetailsList(Page page, Map<String, Object> params);
}

