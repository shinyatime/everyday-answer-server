package io.renren.modules.exam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.renren.modules.exam.entity.ExamIntegralCountEntity;
import io.renren.modules.exam.vo.ExamIntegralCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 
 * 
 * @author tinu
 * @email 1810989@qq.com
 * @date 2019-07-14 12:07:49
 */
@Mapper
public interface ExamIntegralCountDao extends BaseMapper<ExamIntegralCountEntity> {

    IPage<ExamIntegralCountVO> getIntegralCountList(IPage<ExamIntegralCountVO> page, @Param("map") Map<String, Object> map);
	
}
