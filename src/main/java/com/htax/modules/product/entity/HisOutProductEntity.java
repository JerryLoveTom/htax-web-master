package com.htax.modules.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 出货记录表
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-07-08 09:37:50
 */
@Data
@TableName("his_out_product")
public class HisOutProductEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 购买时间
	 */
	private Date reDate;
	/**
	 * 购买物品
	 */
	private String thing;
	/**
	 * 顾客编号
	 */
	private Long customerCode;
	/**
	 * 备注
	 */
	private String remark;

}
