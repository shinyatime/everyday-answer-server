package io.renren.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.exam.entity.ExamQuestionidEntity;

import java.util.Map;

/**
 * 
 *
 * @author tinu
 * @email 1810989@qq.com
 * @date 2019-07-14 12:57:06
 */
public interface ExamQuestionidService extends IService<ExamQuestionidEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

