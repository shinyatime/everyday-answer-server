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
 * @date 2019-07-13 13:00:08
 */
@Data
@TableName("exam_user")
public class ExamUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private String openid;
	/**
	 * 
	 */
	private String userName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 状态 0：无效 1：有效
	 */
	private String status;

}
