package com.htax.modules.txrh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.modules.txrh.entity.vo.SelectOptionVo;
import com.htax.modules.txrh.entity.vo.TxrhDBTabelVo;

import java.util.List;
import java.util.Map;

public interface TxrhDBService {
    Page<TxrhDBTabelVo> queryList(Long current, Long limit, TxrhDBTabelVo search);
    Page<TxrhDBTabelVo> slave1QueryList(Long current, Long limit, TxrhDBTabelVo search);
    Page<TxrhDBTabelVo> slave2QueryList(Long current, Long limit, TxrhDBTabelVo search);
    List<TxrhDBTabelVo> masterList();
    List<TxrhDBTabelVo> slave1SelOptions();
    List<TxrhDBTabelVo> slave2SelOptions();
    List<TxrhDBTabelVo>masterQueryColumns(String tableName);
    List<TxrhDBTabelVo>slave1QueryColumns(String tableName);
    List<TxrhDBTabelVo>slave2QueryColumns(String tableName);
}
