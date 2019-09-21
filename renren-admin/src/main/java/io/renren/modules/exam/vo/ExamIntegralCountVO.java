package io.renren.modules.exam.vo;

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
public class ExamIntegralCountVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;

	private String userName;
	/**
	 * 联合id
	 */
	private String unionid;
	/**
	 * 总积分
	 */
	private Integer integralCount;
	/**
	 * 活动id
	 */
	private Long activityid;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date updateTime;

}
