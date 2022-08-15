package com.htax.modules.txrh.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 多数据源配置信息
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-08 15:03:11
 */
@Data
@TableName("txrh_db_source")
public class TxrhDbSourceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 数据源名称
	 */
	private String slaveName;
	/**
	 * 驱动
	 */
	private String driverClassName;
	/**
	 * 连接数据源地址
	 */
	private String url;
	/**
	 * 连接数据源用户名
	 */
	private String username;
	/**
	* 上级节点
	* */
	private String pid;
	/**
	 * 数据源模型名称
	 * */
	private String mxMc;
	// 目录节点 0目录 1节点
	private String mlJd;
	/**
	 * 连接数据源密码
	 */
	private String password;
	/**
	 * 创建人
	 */
	private Long createUser;
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
