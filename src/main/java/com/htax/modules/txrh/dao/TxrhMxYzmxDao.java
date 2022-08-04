package com.htax.modules.txrh.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.modules.txrh.entity.TxrhMxYzmxEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 原子模型基础表
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-02 19:50:21
 */
@Mapper
public interface TxrhMxYzmxDao extends BaseMapper<TxrhMxYzmxEntity> {

    Page<TxrhMxYzmxEntity> queryPage(Page<TxrhMxYzmxEntity> page, @Param("yzmx") TxrhMxYzmxEntity search);
}
