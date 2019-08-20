package io.renren.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.RedisUtils;
import io.renren.modules.exam.dao.ExamQuestionDao;
import io.renren.modules.exam.entity.*;
import io.renren.modules.exam.service.ExamIntegralDetailsService;
import io.renren.modules.exam.service.ExamQuestionService;
import io.renren.modules.exam.service.ExamQuestionidService;
import io.renren.modules.exam.service.ExamUserQuestionService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;


@Service("examQuestionService")
public class ExamQuestionServiceImpl extends ServiceImpl<ExamQuestionDao, ExamQuestionEntity> implements ExamQuestionService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ExamUserQuestionService examUserQuestionService;

    @Autowired
    private ExamQuestionidService examQuestionidService;

    @Autowired
    private ExamIntegralDetailsService examIntegralDetailsService;

    @Override
    public boolean saveEveryDayQuestion() {
        redisUtils.delete("question_list","question_ids");
        Date nowDate = new Date();
        String ydate = DateUtils.format(DateUtils.addDateDays(nowDate,-1));
        QueryWrapper<ExamQuestionidEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("question_date",ydate);
        ExamQuestionidEntity examQuestionidEntity = examQuestionidService.getOne(wrapper);


        Long questionid = 0L;
        if (examQuestionidEntity != null) {
            questionid = examQuestionidEntity.getQuestionEndid();
        }

        QueryWrapper<ExamQuestionEntity> wrapper1 = new QueryWrapper<>();
        wrapper1.gt("id",questionid);
        wrapper1.eq("status","1");
        wrapper1.orderByAsc("id");
        wrapper1.last("limit 5");
        List<ExamQuestionEntity> list = super.list(wrapper1);
        if (list.size()==0) {
            return false;
        }

        List<Object> ids = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        Long maxid = 0L;
        for (int i = 0 ; i < list.size() ; i++) {

            ExamQuestionEntity examQuestionEntity = list.get(i);
            map.put(String.valueOf(examQuestionEntity.getId()),examQuestionEntity);
            ids.add(examQuestionEntity.getId()+"");
            if (i == (list.size()-1)) {
                maxid = examQuestionEntity.getId();
            }
        }

        if (maxid != 0) {
            wrapper = new QueryWrapper<>();
            wrapper.eq("question_date",DateUtils.format(nowDate));
            examQuestionidEntity = examQuestionidService.getOne(wrapper);
            if (examQuestionidEntity == null) {
                examQuestionidEntity = new ExamQuestionidEntity();
                examQuestionidEntity.setQuestionEndid(maxid);
                examQuestionidEntity.setQuestionDate(nowDate);
                examQuestionidService.saveOrUpdate(examQuestionidEntity);
            }
        }

        redisUtils.setHash("question_list",map);
        redisUtils.setList("question_ids",ids);
        return true;
    }

    public boolean getRandomEveryDayQuestion(){
        redisUtils.delete("question_list","question_ids");
        QueryWrapper<ExamQuestionEntity> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("status","1");
        List<ExamQuestionEntity> list = super.list(wrapper1);
        if (list.size()==0) {
            return false;
        }
        if (list.size()>5) {
            int num = list.size() - 5;
            for (int i = 0 ; i < num ; i++) {
                list.remove(new Random().nextInt(list.size()));
            }
        }

        List<Object> ids = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        for (int i = 0 ; i < list.size() ; i++) {
            ExamQuestionEntity examQuestionEntity = list.get(i);
            map.put(String.valueOf(examQuestionEntity.getId()),examQuestionEntity);
            ids.add(examQuestionEntity.getId()+"");
        }

        redisUtils.setHash("question_list",map);
        redisUtils.setList("question_ids",ids);
        //System.out.println(redisUtils.getList("question_ids"));
        return true;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExamQuestionEntity> page = this.page(
                new Query<ExamQuestionEntity>().getPage(params),
                new QueryWrapper<ExamQuestionEntity>().orderByDesc("id")
        );

        return new PageUtils(page);
    }

    @Override
    public String saveUserQuestion(ExamUserEntity user) {
        String openid = user.getOpenid();
        QueryWrapper<ExamUserQuestionEntity> wrapper = new QueryWrapper<>();
        wrapper.select("max(answer_count) answer_count");
        //wrapper.ge("create_time", DateUtils.format(new Date())+" 00:00:00");
        //wrapper.le("create_time", DateUtils.format(new Date())+" 23:59:59");
        wrapper.eq("DATE_FORMAT(create_time,'%Y-%m-%d')",DateUtils.format(new Date()));
        wrapper.eq("openid",openid);
        ExamUserQuestionEntity oldUq = examUserQuestionService.getOne(wrapper);
        if (oldUq != null) {
           throw new RRException("今日答题完毕！");
        }
//        if(openid==null||"".equals(openid)) {//参数错误
//            return R.error("参数格式错误");
//        }
//        String userid = (String) redisUtils.getHash("regUserList", openid);
//
//        if ("".equals(userid)) {
//            return R.error("用户信息错误");
//        }
        List<Object> questionids = redisUtils.getList("question_ids");

        int num = 0;
        String firstquestionid = "";
        //存入用户明细
        for(Object id : questionids) {
            ExamQuestionEntity questionEntity = (ExamQuestionEntity) redisUtils.getHash("question_list",String.valueOf(id));
            ExamUserQuestionEntity uq = new ExamUserQuestionEntity();
            num = num + 1;
            uq.setOpenid(openid);
            uq.setUserId(Long.valueOf(user.getId()));
            uq.setQuestionid(questionEntity.getId());
            uq.setAnswerOrder(num);
            uq.setAnswerCount(1);
            uq.setCreateTime(new Date());
            examUserQuestionService.save(uq);
            redisUtils.setList("user_"+openid,questionEntity.getId());
            if(num == 1) {
                firstquestionid = String.valueOf(questionEntity.getId());
            }
        }

        ExamIntegralDetailsEntity detailsEntity = new ExamIntegralDetailsEntity();
        detailsEntity.setUserId(Long.valueOf(user.getId()));
        detailsEntity.setOpenid(openid);
        detailsEntity.setContent("每日答题");
        detailsEntity.setIntegral(0);
        detailsEntity.setIntegralType("0");
        detailsEntity.setCreateTime(new Date());
        detailsEntity.setCount(questionids.size());
        detailsEntity.setRightNum(0);
        detailsEntity.setFinishNum(0);
        examIntegralDetailsService.save(detailsEntity);

        Long datediff = DateUtils.getBetweenSecond(new Date(),DateUtils.getEndTime());
        redisUtils.expire("user_"+openid,datediff);
        return firstquestionid;
    }

    @Override
    public boolean batchImport(String fileName, MultipartFile file) throws Exception {
        boolean notNull = false;
        //List<ExamQuestionEntity> questionList = new ArrayList<>();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new RRException("上传文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if(sheet!=null){
            notNull = true;
        }
        //String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
        ExamQuestionEntity questionEntity;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null){
                continue;
            }

            questionEntity = new ExamQuestionEntity();

            //1.类型
            if( row.getCell(0).getCellType() !=1){
                throw new RRException("导入失败(第"+(r+1)+"行,类型请设为文本格式)");
            }
            String type = row.getCell(0).getStringCellValue();
            if(type == null || type.isEmpty()){
                throw new RRException("导入失败(第"+(r+1)+"行,类型未填写)");
            }
            //1：单选题 2：多选题 3：填空题
            if ("单选题".equals(type)) {
                type = "1";
            } else if ("多选题".equals(type)) {
                type = "2";
            } else if ("填空题".equals(type)) {
                type = "3";
            }
            questionEntity.setType(type);


            //2.题目
            if( row.getCell(1).getCellType() !=1){
                throw new RRException("导入失败(第"+(r+1)+"行,题目请设为文本格式)");
            }
            String stem = row.getCell(1).getStringCellValue();
            if(stem == null || stem.isEmpty()){
                throw new RRException("导入失败(第"+(r+1)+"行,题目未填写)");
            }
            questionEntity.setStem(stem);

            //3.选择题答案
            if( row.getCell(2).getCellType() !=1){
                throw new RRException("导入失败(第"+(r+1)+"行,选择题答案请设为文本格式)");
            }
            String metas = row.getCell(2).getStringCellValue();
            if ("1".equals(type) || "2".equals(type)) {
                if (metas == null || metas.isEmpty()) {
                    throw new RRException("导入失败(第" + (r + 1) + "行,选择题答案未填写)");
                }
            }
            questionEntity.setMetas(metas);

            //4.分数
            if( row.getCell(3).getCellType() !=1){
                throw new RRException("导入失败(第"+(r+1)+"行,分数请设为文本格式)");
            }
            String score = row.getCell(3).getStringCellValue();

            if (score == null || score.isEmpty()) {
                //throw new RRException("导入失败(第" + (r + 1) + "行,分数未填写)");
                score = "0";
            }
            questionEntity.setScore(Integer.valueOf(score));

            //5.正确答案
            if( row.getCell(4).getCellType() !=1){
                throw new RRException("导入失败(第"+(r+1)+"行,正确答案请设为文本格式)");
            }
            String answer = row.getCell(4).getStringCellValue();

            if (answer == null || answer.isEmpty()) {
                throw new RRException("导入失败(第" + (r + 1) + "行,正确答案未填写)");
            }
            questionEntity.setAnswer(answer);

            //6.解析
            if( row.getCell(5).getCellType() !=1){
                throw new RRException("导入失败(第"+(r+1)+"行,解析请设为文本格式)");
            }
            String analysis = row.getCell(5).getStringCellValue();

            if (analysis == null || analysis.isEmpty()) {
                questionEntity.setAnalysis("无");
            } else {
                questionEntity.setAnalysis(analysis);
            }

            //7.提示
            if( row.getCell(6).getCellType() !=1){
                throw new RRException("导入失败(第"+(r+1)+"行,提示请设为文本格式)");
            }
            String tips = row.getCell(6).getStringCellValue();

            if (analysis == null || analysis.isEmpty()) {
                questionEntity.setTips("无");
            } else {
                questionEntity.setTips(tips);
            }
            questionEntity.setCreateTime(new Date());
            questionEntity.setCreateUser("导入");
            //questionList.add(questionEntity);
            //System.out.println(questionEntity);
            baseMapper.insert(questionEntity);
        }
//        for (User userResord : userList) {
//            String name = userResord.getName();
//            int cnt = userMapper.selectByName(name);
//            if (cnt == 0) {
//                userMapper.addUser(userResord);
//                System.out.println(" 插入 "+userResord);
//            } else {
//                userMapper.updateUserByName(userResord);
//                System.out.println(" 更新 "+userResord);
//            }
//        }
        return notNull;
    }

}
