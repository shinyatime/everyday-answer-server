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
 * @date 2019-07-12 10:02:00
 */
@Data
@TableName("exam_question")
public class ExamQuestionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 活动id
	 */
	private Long activityid;
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
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建人
	 */
	private String createUser;
	/**
	 * 状态 0：无效 1：有效
	 */
	private String status;
	/**
	 * 分类
	 */
	private String tag;
	/**
	 * 提示
	 */
	private String tips;

}
