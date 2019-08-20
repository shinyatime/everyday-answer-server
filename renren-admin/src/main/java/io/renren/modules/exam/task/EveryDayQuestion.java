package io.renren.modules.exam.task;

import io.renren.modules.exam.service.ExamQuestionService;
import io.renren.modules.job.task.ITask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("everyDayQuestion")
public class EveryDayQuestion implements ITask {

    @Autowired
    private ExamQuestionService examQuestionService;


    @Override
    public void run(String params) {
        examQuestionService.saveEveryDayQuestion();
        //System.out.println("11111111111111111111111111");
    }
}
