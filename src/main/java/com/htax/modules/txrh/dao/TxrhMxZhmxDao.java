package com.htax.modules.txrh.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.modules.txrh.entity.TxrhMxZhmxEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组合模型
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-02 19:47:02
 */
@Mapper
public interface TxrhMxZhmxDao extends BaseMapper<TxrhMxZhmxEntity> {
    // 获取 列表
    List<TxrhMxZhmxEntity> getMineAndPublic(@Param("search") TxrhMxZhmxEntity search);

    // 获取组合模型信息，包含创建者名称
    TxrhMxZhmxEntity getInfoById(@Param("id") String id);

    Page<TxrhMxZhmxEntity> queryPages(Page<TxrhMxZhmxEntity> page, @Param("search") TxrhMxZhmxEntity search);

}
