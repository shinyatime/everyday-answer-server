package io.renren.modules.exam.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.exam.entity.ExamUserQuestionEntity;
import io.renren.modules.exam.service.ExamUserQuestionService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author tinu
 * @email 1810989@qq.com
 * @date 2019-07-14 12:07:49
 */
@RestController
@RequestMapping("exam/examuserquestion")
public class ExamUserQuestionController {
    @Autowired
    private ExamUserQuestionService examUserQuestionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("exam:examuserquestion:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = examUserQuestionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("exam:examuserquestion:info")
    public R info(@PathVariable("id") Long id){
        ExamUserQuestionEntity examUserQuestion = examUserQuestionService.getById(id);

        return R.ok().put("examUserQuestion", examUserQuestion);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("exam:examuserquestion:save")
    public R save(@RequestBody ExamUserQuestionEntity examUserQuestion){
        examUserQuestionService.save(examUserQuestion);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("exam:examuserquestion:update")
    public R update(@RequestBody ExamUserQuestionEntity examUserQuestion){
        ValidatorUtils.validateEntity(examUserQuestion);
        examUserQuestionService.updateById(examUserQuestion);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("exam:examuserquestion:delete")
    public R delete(@RequestBody Long[] ids){
        examUserQuestionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
