package io.renren.modules.exam.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.exam.entity.ExamUserEntity;
import io.renren.modules.exam.vo.UserQuestionVO;

import java.util.Map;

/**
 * 
 *
 * @author tinu
 * @email 1810989@qq.com
 * @date 2019-07-13 13:00:08
 */
public interface ExamUserService extends IService<ExamUserEntity> {

    PageUtils queryPage(Map<String, Object> params);



}

