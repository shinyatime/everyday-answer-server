package io.renren.modules.exam.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.exam.entity.ExamUserQuestionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.exam.vo.UserQuestionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author tinu
 * @email 1810989@qq.com
 * @date 2019-07-14 12:07:49
 */
@Mapper
public interface ExamUserQuestionDao extends BaseMapper<ExamUserQuestionEntity> {

    IPage<UserQuestionVO> getUserQuestion(Page page, @Param("map") Map<String,Object> map);

    UserQuestionVO getUserQuestionOne(Long id);
}
