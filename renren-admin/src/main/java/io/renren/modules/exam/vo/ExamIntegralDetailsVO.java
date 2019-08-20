package io.renren.modules.exam.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tinu
 * @email 1810989@qq.com
 * @date 2019-07-14 12:07:49
 */
@Data
public class ExamIntegralDetailsVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 用户id
     */
    private Long userId;


    private String userName;
    /**
     * openid
     */
    private String openid;
    /**
     * 联合id
     */
    private Long unionid;
    /**
     * 积分描述
     */
    private String content;
    /**
     * 积分
     */
    private Integer integral;
    /**
     * 积分类型
     */
    private String integralType;
    /**
     * 活动id
     */
    private Long activityid;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 每日答题总数
     */
    private Integer count;

    /**
     * 正确数
     */
    private Integer rightNum;

    /**
     * 完成数
     */
    private Integer finishNum;

}
