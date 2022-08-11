package com.htax.modules.txrh.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 组合模型节点数据源
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-09 15:13:52
 */
@Data
@TableName("txrh_zhmx_jd_sjy")
public class TxrhZhmxJdSjyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 外键，节点编号 txrh_zhmx_jd
	 */
	private String jdId;
	/**
	 * 外键，组合模型编号 txrh_mx_zhmx
	 */
	private String zhmxId;
	/**
	 * 外键，数据源id txrh_db_source
	 */
	private String dbId;
	/**
	 * 操作
	 * */
	@TableField(exist = false)
	private String operate;
	/**
	 * 数据库表名
	 */
	private String tableName;
	/**
	 * 数据源条件
	 */
	@TableField("`condition`")
	private String condition;

	/**
	 * 创建人
	 */
	private Long createUser;

	// @TableLogic(value = "0",delval = "1") // 逻辑删除 新版本只需要加入这个，不需要进行config配置
	@ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
	private Boolean isDelete;

	@ApiModelProperty(value = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

	@ApiModelProperty(value = "更新时间")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

}
