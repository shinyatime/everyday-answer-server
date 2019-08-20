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

import io.renren.modules.exam.entity.ExamUserEntity;
import io.renren.modules.exam.service.ExamUserService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author tinu
 * @email 1810989@qq.com
 * @date 2019-07-13 13:00:08
 */
@RestController
@RequestMapping("exam/examuser")
public class ExamUserController {
    @Autowired
    private ExamUserService examUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("exam:examuser:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = examUserService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("exam:examuser:info")
    public R info(@PathVariable("id") Long id){
        ExamUserEntity examUser = examUserService.getById(id);

        return R.ok().put("examUser", examUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("exam:examuser:save")
    public R save(@RequestBody ExamUserEntity examUser){
        examUserService.save(examUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("exam:examuser:update")
    public R update(@RequestBody ExamUserEntity examUser){
        ValidatorUtils.validateEntity(examUser);
        examUserService.updateById(examUser);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("exam:examuser:delete")
    public R delete(@RequestBody Long[] ids){
        examUserService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
