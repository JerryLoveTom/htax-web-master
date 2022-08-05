package com.htax.modules.txrh.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 原子算法入参参数表
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-02 19:56:55
 */
@Data
@TableName("txrh_yzmx_rcs")
public class TxrhYzmxRcsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 外键，模型ID
	 */
	private String mxId;
	/**
	 * 参数中文名称
	 */
	private String csZwMc;
	/**
	 * 参数英文名称
	 */
	private String csYwMc;
	/**
	 * 参数类型:0.int 1.string 2.boolen 3.list 4.float
	 */
	private String csLx;
	/**
	 * 方法运行总耗时
	 */
	private Integer yxHs;
	@TableLogic(value = "0",delval = "1") // 逻辑删除 新版本只需要加入这个，不需要进行config配置
	@ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
	private Boolean isDelete;

	@ApiModelProperty(value = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

	@ApiModelProperty(value = "更新时间")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

}
