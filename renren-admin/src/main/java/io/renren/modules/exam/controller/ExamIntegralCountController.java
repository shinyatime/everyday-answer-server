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

import io.renren.modules.exam.entity.ExamIntegralCountEntity;
import io.renren.modules.exam.service.ExamIntegralCountService;
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
@RequestMapping("exam/examintegralcount")
public class ExamIntegralCountController {
    @Autowired
    private ExamIntegralCountService examIntegralCountService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("exam:examintegralcount:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = examIntegralCountService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("exam:examintegralcount:info")
    public R info(@PathVariable("id") Long id){
        ExamIntegralCountEntity examIntegralCount = examIntegralCountService.getById(id);

        return R.ok().put("examIntegralCount", examIntegralCount);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("exam:examintegralcount:save")
    public R save(@RequestBody ExamIntegralCountEntity examIntegralCount){
        examIntegralCountService.save(examIntegralCount);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("exam:examintegralcount:update")
    public R update(@RequestBody ExamIntegralCountEntity examIntegralCount){
        ValidatorUtils.validateEntity(examIntegralCount);
        examIntegralCountService.updateById(examIntegralCount);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("exam:examintegralcount:delete")
    public R delete(@RequestBody Long[] ids){
        examIntegralCountService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
