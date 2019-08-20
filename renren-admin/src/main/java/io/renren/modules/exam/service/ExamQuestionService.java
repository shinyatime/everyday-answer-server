package io.renren.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.exam.entity.ExamQuestionEntity;
import io.renren.modules.exam.entity.ExamUserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 
 *
 * @author tinu
 * @email 1810989@qq.com
 * @date 2019-07-12 10:02:00
 */
public interface ExamQuestionService extends IService<ExamQuestionEntity> {

    boolean saveEveryDayQuestion();

    PageUtils queryPage(Map<String, Object> params);

    String saveUserQuestion(ExamUserEntity user);

    boolean batchImport(String fileName, MultipartFile file) throws Exception;
}

