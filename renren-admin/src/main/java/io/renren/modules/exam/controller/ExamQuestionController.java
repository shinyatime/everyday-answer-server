package io.renren.modules.exam.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.exception.RRException;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.exam.entity.ExamQuestionEntity;
import io.renren.modules.exam.service.ExamQuestionService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 *
 * @author tinu
 * @email 1810989@qq.com
 * @date 2019-07-12 10:02:00
 */
@RestController
@RequestMapping("exam/examquestion")
public class ExamQuestionController {
    @Autowired
    private ExamQuestionService examQuestionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("exam:examquestion:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = examQuestionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("exam:examquestion:info")
    public R info(@PathVariable("id") Long id){
        ExamQuestionEntity examQuestion = examQuestionService.getById(id);

        return R.ok().put("examQuestion", examQuestion);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("exam:examquestion:save")
    public R save(@RequestBody ExamQuestionEntity examQuestion){
        String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
        examQuestion.setCreateUser(username);
        examQuestion.setCreateTime(new Date());
        examQuestionService.save(examQuestion);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("exam:examquestion:update")
    public R update(@RequestBody ExamQuestionEntity examQuestion){
        ValidatorUtils.validateEntity(examQuestion);
        examQuestionService.updateById(examQuestion);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("exam:examquestion:delete")
    public R delete(@RequestBody Long[] ids){
        ExamQuestionEntity examQuestionEntity = new ExamQuestionEntity();
        examQuestionEntity.setStatus("0");
        examQuestionService.update(examQuestionEntity,new QueryWrapper<ExamQuestionEntity>().in("id",Arrays.asList(ids)));

        return R.ok();
    }

    /**
     * 恢复
     * @param ids
     * @return
     */
    @RequestMapping("/restore")
    @RequiresPermissions("exam:examquestion:restore")
    public R restore(@RequestBody Long[] ids){
        ExamQuestionEntity examQuestionEntity = new ExamQuestionEntity();
        examQuestionEntity.setStatus("1");
        examQuestionService.update(examQuestionEntity,new QueryWrapper<ExamQuestionEntity>().in("id",Arrays.asList(ids)));

        return R.ok();
    }

    @PostMapping("/import")
    @RequiresPermissions("exam:examquestion:import")
    public R addQuestion(@RequestParam("file") MultipartFile file) {

        String fileName = file.getOriginalFilename();
        try {
            examQuestionService.batchImport(fileName, file);
        } catch (Exception e) {
            throw new RRException(e.getMessage());
        }

        return R.ok();
    }

    @GetMapping("/catchQuestion")
    @RequiresPermissions("exam:examquestion:catchQuestion")
    public R catchQuestion() {

        boolean flag = examQuestionService.saveEveryDayQuestion();
        if (!flag) {
            return R.error("缓存每日题目失败！");
        }

        return R.ok();
    }

}
