package io.renren.modules.exam.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserQuestionVO implements Serializable {

    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * openid
     */
    private String openid;
    /**
     * 问题id
     */
    private Long questionid;
    /**
     * 答案
     */
    private String userAnswer;
    /**
     * 序号
     */
    private Integer answerOrder;
    /**
     * 分数
     */
    private Integer integral;
    /**
     * 状态
     */
    private String status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 题目类型 1：单选题 2：多选题 3：填空题
     */
    private String type;
    /**
     * 题干
     */
    private String stem;
    /**
     * 备选答案
     */
    private String metas;
    /**
     * 分数
     */
    private Integer score;
    /**
     * 正确答案
     */
    private String answer;
    /**
     * 解析
     */
    private String analysis;
    /**
     * 提示
     */
    private String tips;
}
