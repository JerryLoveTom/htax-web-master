package com.htax.modules.txrh.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.common.constant.Constants;
import com.htax.common.utils.R;
import com.htax.modules.txrh.entity.TxrhDbSourceEntity;
import com.htax.modules.txrh.entity.vo.SelectOptionVo;
import com.htax.modules.txrh.entity.vo.TxrhDBTabelVo;
import com.htax.modules.txrh.service.TxrhDBService;
import com.htax.modules.txrh.service.TxrhDbSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: htax-web-master
 * @BelongsPackage: com.htax.modules.txrh.controller
 * @Author: ppzz
 * @CreateTime: 2022-08-08  13:06
 * @Description: TODO: 数据库表
 * @Version: 1.0
 */
@RestController
@RequestMapping("txrh/db")
@Api("数据库表")
public class TxrhDBController {
    @Autowired
    private TxrhDbSourceService txrhDbSourceService; // 数据源
    @Autowired
    private TxrhDBService dbService; // 表


    @GetMapping("/source/list/{current}/{limit}")
    @ApiOperation("获取数据源列表")
    public R sourceList(@ApiParam(name = "current",value = "当前页码") @PathVariable Long current
            , @ApiParam(name = "limit",value = "每页记录数")@PathVariable Long limit
            , TxrhDbSourceEntity search){
        search.setMlJd("1"); // 只查节点
        Page<TxrhDbSourceEntity> items = txrhDbSourceService.queryPage(current, limit, search);

        return R.ok().put("items",items);
    }

    @GetMapping("/querylist/{current}/{limit}")
    @ApiOperation("获取列表")
    public R queryList(@ApiParam(name = "current",value = "当前页码") @PathVariable Long current
            , @ApiParam(name = "limit",value = "每页记录数")@PathVariable Long limit
            , TxrhDBTabelVo search){
        Page<TxrhDBTabelVo> list = dbService.queryList(current,limit, search);
        Page<TxrhDBTabelVo> slave1List = dbService.slave1QueryList(current,limit, search);
        Page<TxrhDBTabelVo> slave2List = dbService.slave2QueryList(current,limit, search);
        return R.ok().put("items",list).put("slaves1",slave1List).put("slaves2",slave2List);
    }
    /**
     * 通过数据库名获取相关的表名称
     * */
    @GetMapping("/table/option/{sourceid}")
    @ApiOperation("获取列表")
    public R queryList(@PathVariable("sourceid") String sourceid){
        List<TxrhDBTabelVo>list = new ArrayList<>();

        switch (sourceid){
            case  Constants.DATASOURCE_MASTER:
                list = dbService.masterList();
                break;
            case Constants.DATASOURCE_SLAVE1:
                list = dbService.slave1SelOptions();
                break;
            case Constants.DATASOURCE_SLAVE2:
                list = dbService.slave2SelOptions();
                break;
            default:
                list = dbService.masterList();
                break;
        }
        return R.ok().put("items",list);
    }
    @GetMapping("/table/columnname/{sourceid}/{tablename}")
    @ApiOperation("根据数据源和表名获取表属性字段信息")
    public R queryColumnName(@PathVariable("sourceid") String sourceid
            ,@PathVariable("tablename") String tablename){
        List<TxrhDBTabelVo>list = new ArrayList<>();

        switch (sourceid){
            case  Constants.DATASOURCE_MASTER:
                list = dbService.masterQueryColumns(tablename);
                break;
            case Constants.DATASOURCE_SLAVE1:
                list = dbService.slave1QueryColumns(tablename);
                break;
            case Constants.DATASOURCE_SLAVE2:
                list = dbService.slave2QueryColumns(tablename);
                break;
            default:
                list = dbService.masterQueryColumns(tablename);
                break;
        }
        return R.ok().put("items",list);
    }

}
