package com.htax.modules.txrh.dao;

import com.htax.modules.txrh.entity.TxrhDbSourceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 多数据源配置信息
 * 
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-08 15:03:11
 */
@Mapper
public interface TxrhDbSourceDao extends BaseMapper<TxrhDbSourceEntity> {
	
}
