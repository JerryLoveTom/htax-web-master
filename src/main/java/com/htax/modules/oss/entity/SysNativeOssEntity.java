package com.htax.modules.oss.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 本地文件存储记录表
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-06-18 14:58:34
 */
@Data
@TableName("sys_native_oss")
public class SysNativeOssEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer id;
	/**
	 * URL访问地址
	 */
	private String url;
	/**
	 * 是否删除 0 否  1  是
	 */
	@TableLogic(value = "0",delval = "1")
	private Integer isDelete;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

}
