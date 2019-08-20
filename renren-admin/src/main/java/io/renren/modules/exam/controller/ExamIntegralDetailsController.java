package io.renren.modules.exam.controller;

import java.util.Arrays;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.utils.DateUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.exam.vo.ExamIntegralDetailsVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.exam.entity.ExamIntegralDetailsEntity;
import io.renren.modules.exam.service.ExamIntegralDetailsService;
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
@RequestMapping("exam/examintegraldetails")
public class ExamIntegralDetailsController {
    @Autowired
    private ExamIntegralDetailsService examIntegralDetailsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("exam:examintegraldetails:list")
    public R list(@RequestParam Map<String, Object> params){

        int current = params.containsKey("page")?Integer.valueOf(params.get("page").toString()):0;
        int sizt = params.containsKey("limit")?Integer.valueOf(params.get("limit").toString()):2;

        //PageUtils page = examIntegralDetailsService.queryPage(params);
        String startTime = params.containsKey("startTime")?params.get("startTime").toString():"";
        String endTime = params.containsKey("endTime")?params.get("endTime").toString():"";
        if (!"".equals(startTime) && startTime.length()>=10) {
            startTime = startTime.substring(0,10) + " 00:00:00";
        }
        if (!"".equals(endTime) && endTime.length()>=10) {
            endTime = endTime.substring(0,10) + " 23:59:59";
        }
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        IPage<ExamIntegralDetailsVO> ipage = examIntegralDetailsService.getIntegralDetailsList(new Page(current,sizt),params);
        PageUtils page = new PageUtils(ipage);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("exam:examintegraldetails:info")
    public R info(@PathVariable("id") Long id){
        ExamIntegralDetailsEntity examIntegralDetails = examIntegralDetailsService.getById(id);

        return R.ok().put("examIntegralDetails", examIntegralDetails);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("exam:examintegraldetails:save")
    public R save(@RequestBody ExamIntegralDetailsEntity examIntegralDetails){
        examIntegralDetailsService.save(examIntegralDetails);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("exam:examintegraldetails:update")
    public R update(@RequestBody ExamIntegralDetailsEntity examIntegralDetails){
        ValidatorUtils.validateEntity(examIntegralDetails);
        examIntegralDetailsService.updateById(examIntegralDetails);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("exam:examintegraldetails:delete")
    public R delete(@RequestBody Long[] ids){
        examIntegralDetailsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
