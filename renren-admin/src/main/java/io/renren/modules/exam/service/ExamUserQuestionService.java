package io.renren.modules.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.exam.entity.ExamUserQuestionEntity;
import io.renren.modules.exam.vo.UserQuestionVO;

import java.util.Map;

/**
 * 
 *
 * @author tinu
 * @email 1810989@qq.com
 * @date 2019-07-14 12:07:49
 */
public interface ExamUserQuestionService extends IService<ExamUserQuestionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    boolean saveAnswer(ExamUserQuestionEntity userQuestionEntity);

    IPage<UserQuestionVO> getUserQuestion(Page page, Map<String, Object> map);

    UserQuestionVO getUserQuestionOne(Long id);
}

