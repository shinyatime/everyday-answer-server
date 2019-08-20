package io.renren.modules.exam.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author tinu
 * @email 1810989@qq.com
 * @date 2019-07-14 12:07:49
 */
@Data
@TableName("exam_user_question")
public class ExamUserQuestionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 联合id
	 */
	private String unionid;
	/**
	 * openid
	 */
	private String openid;
	/**
	 * 活动id
	 */
	private Long activityid;
	/**
	 * 问题id
	 */
	private Long questionid;
	/**
	 * 答案
	 */
	private String answer;
	/**
	 * 序号
	 */
	private Integer answerOrder;
	/**
	 * 每日次数
	 */
	private Integer answerCount;
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

}
