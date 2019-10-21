package io.renren.modules;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.exam.entity.ExamUserEntity;
import io.renren.modules.exam.service.ExamQuestionService;
import io.renren.modules.exam.service.ExamUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Order(value = 2)
public class QuestionRunner implements ApplicationRunner {

//    @Autowired
//    private RedisUtils redisUtils;

//    @Autowired
//    private ExamUserService examUserService;

    @Autowired
    private ExamQuestionService examQuestionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //initRedisUser();

        initEveryDayQuestion();

    }

    private void initEveryDayQuestion() {
        //examQuestionService.saveEveryDayQuestion();

        examQuestionService.getRandomEveryDayQuestion();
    }


    private void initRedisUser() {
//        redisUtils.delete("regUserList");
//        QueryWrapper<ExamUserEntity> wrapper = new QueryWrapper<>();
//        wrapper.eq("status","1");
//        List<ExamUserEntity> list = examUserService.list(wrapper);
//        Map<String,Object> map = new HashMap<>();
//        for(ExamUserEntity examUserEntity:list) {
//            map.put(examUserEntity.getOpenid(),examUserEntity.getId());
//        }
//        redisUtils.setHash("regUserList",map);
    }

    public static void main(String[] args) {
        String ydate = DateUtils.format(DateUtils.addDateDays(new Date(),-1));
        System.out.println(ydate);
    }
}
