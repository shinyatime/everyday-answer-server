package io.renren.modules.exam.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.exam.entity.ExamIntegralDetailsEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.exam.vo.ExamIntegralDetailsVO;
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
public interface ExamIntegralDetailsDao extends BaseMapper<ExamIntegralDetailsEntity> {

    IPage<ExamIntegralDetailsVO> getIntegralDetailsList(IPage<ExamIntegralDetailsVO> page, @Param("map") Map<String, Object> map);
}
