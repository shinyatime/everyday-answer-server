package io.renren.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.exam.entity.ExamIntegralCountEntity;

import java.util.Map;

/**
 * 
 *
 * @author tinu
 * @email 1810989@qq.com
 * @date 2019-07-14 12:07:49
 */
public interface ExamIntegralCountService extends IService<ExamIntegralCountEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

