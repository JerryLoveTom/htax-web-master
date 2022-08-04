package com.htax.modules.txrh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 组合模型中原子模型的连线
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-02 19:47:02
 */
@Data
@TableName("txrh_zhmx_lx")
public class TxrhZhmxLxEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.INPUT)
	private Long id;
	/**
	 * 外键，组合模型的id
	 */
	private Long zhmxId;
	/**
	 * 开始节点ID
	 */
	private String from;
	/**
	 * 结束节点ID
	 */
	private String to;
	/**
	 * 条件
	 */
	private String label;

}
