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

import io.renren.modules.exam.entity.ExamQuestionidEntity;
import io.renren.modules.exam.service.ExamQuestionidService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author tinu
 * @email 1810989@qq.com
 * @date 2019-07-14 12:57:06
 */
@RestController
@RequestMapping("exam/examquestionid")
public class ExamQuestionidController {
    @Autowired
    private ExamQuestionidService examQuestionidService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("exam:examquestionid:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = examQuestionidService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("exam:examquestionid:info")
    public R info(@PathVariable("id") Integer id){
        ExamQuestionidEntity examQuestionid = examQuestionidService.getById(id);

        return R.ok().put("examQuestionid", examQuestionid);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("exam:examquestionid:save")
    public R save(@RequestBody ExamQuestionidEntity examQuestionid){
        examQuestionidService.save(examQuestionid);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("exam:examquestionid:update")
    public R update(@RequestBody ExamQuestionidEntity examQuestionid){
        ValidatorUtils.validateEntity(examQuestionid);
        examQuestionidService.updateById(examQuestionid);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("exam:examquestionid:delete")
    public R delete(@RequestBody Integer[] ids){
        examQuestionidService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
