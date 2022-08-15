package com.htax.modules.txrh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.modules.txrh.entity.TxrhDbSourceEntity;
import com.htax.modules.txrh.entity.vo.TxrhDBTabelVo;
import org.json.JSONArray;

import java.util.List;

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

    // 通过数据源、sql查询数据
    JSONArray getDataBySourceAndTable(TxrhDbSourceEntity dbSource , String sql);
}
