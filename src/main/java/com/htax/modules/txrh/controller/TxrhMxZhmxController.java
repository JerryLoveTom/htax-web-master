package com.htax.modules.txrh.controller;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.htax.common.utils.Constant;
import com.htax.modules.sys.controller.AbstractController;
import com.htax.modules.txrh.entity.*;
import com.htax.modules.txrh.entity.vo.NodeMenuVo;
import com.htax.modules.txrh.entity.vo.ParamFormVo;
import com.htax.modules.txrh.entity.vo.WorkFlowDataVo;
import com.htax.modules.txrh.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.htax.common.utils.R;



/**
 * 组合模型
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-02 19:47:02
 */
@RestController
@RequestMapping("txrh/model/zhmx")
@Api("组合模型")
public class TxrhMxZhmxController extends AbstractController {
    @Autowired
    private TxrhMxZhmxService txrhMxZhmxService;// 组合模型
    @Autowired
    private TxrhMxYzmxService txrhMxYzmxService; // 原子模型
    @Autowired
    private TxrhDbSourceService txrhDbSourceService; // 数据源
    @Autowired
    private TxrhZhmxJdSjyService zhmxJdSjyService;// 数据源服务
    @Autowired
    private TxrhZhmxJdCsdzService zhmxJdCsdzService; // 节点参数对照


    @PostMapping("/jdcsdz")
    @ApiOperation("查询当前节点已配置参数")
    public R saveJdCsdz(@RequestBody ParamFormVo vo){
        vo.getToParams().forEach(item ->{
            if ("1".equals(item.getFromType())){
                item.setFromParam(Long.parseLong(item.getFromColumnName()));
                item.setFromColumnName(null);
            }
        });
        // 先移除to_node
        zhmxJdCsdzService.remove(new QueryWrapper<TxrhZhmxJdCsdzEntity>().eq("to_node",vo.getToParams().get(0).getToNode()));
        zhmxJdCsdzService.saveBatch( vo.getToParams());
        return R.ok();
    }
    /**
     * 查询当前节点已配置参数
     * */
    @GetMapping("/getsetparams/{toId}")
    @ApiOperation("查询当前节点已配置参数")
    public R getSetParams(@PathVariable("toId") String toId){
        List<TxrhZhmxJdCsdzEntity> list = zhmxJdCsdzService.list(new QueryWrapper<TxrhZhmxJdCsdzEntity>().eq("to_node",toId));
        return R.ok().put("items",list);
    }
    /**
     * 查询当前节点的上级节点
     * */
    @GetMapping("/getfromjd/{toId}")
    @ApiOperation("获取数据源绑定的表信息")
    public R getFromjd(@PathVariable("toId") String toId){
        List<TxrhZhmxJdEntity> list = txrhMxZhmxService.getFromJdByToId(toId);
        return R.ok().put("items",list);
    }
    /**
     * 获取数据源绑定的表信息
     * */
    @GetMapping("/source/bind/{sourceid}")
    @ApiOperation("获取数据源绑定的表信息")
    public R getSourceBindInfo(@PathVariable("sourceid") String sourceid){
        TxrhZhmxJdSjyEntity jdSjyEntity = zhmxJdSjyService.getOne(new QueryWrapper<TxrhZhmxJdSjyEntity>().eq("jd_id", sourceid).last("limit 1"));
        return R.ok().put("item",jdSjyEntity);
    }
    /**
     * 工作流运行
     * */
    @GetMapping("/startflow/{id}")
    @ApiOperation("通过id执行工作流")
    public R runWorkFlow(@PathVariable("id") String id){
        txrhMxZhmxService.runWorkFlow(id);
        return R.ok();
    }

    /**
     * 克隆模型信息
     * */
    @GetMapping("/clone/{id}")
    @ApiOperation("克隆模型信息")
    public R clone(@PathVariable("id") String id){
        boolean success = txrhMxZhmxService.cloneFlow(id);
        return R.ok();
    }

    // 保存模型流程信息
    @PostMapping("/flowdata")
    @ApiOperation("保存模型流程信息")
    public R saveFlowData(@RequestBody WorkFlowDataVo entity){
        int result = txrhMxZhmxService.saveFlowData(entity);
        return R.ok();
    }

    // 通过Id 获取组合模型的连线、节点信息
    @GetMapping("/flowdata/{id}")
    @ApiOperation("通过Id 获取组合模型的连线、节点信息")
    public R flowData(@ApiParam(name = "id",value = "组合模型id") @PathVariable String id){
        WorkFlowDataVo item = txrhMxZhmxService.getFlowDataById(id);
        return R.ok().put("item",item);
    }

    /**
     * 获取左侧模型树：可能包含 1.原子模型 2.输入数据源 3.输出
     * */
    @GetMapping("/treelist")
    @ApiOperation("获取原子模型、输入数据源、输出等tree接口模型数据")
    public R treeList(TxrhMxYzmxEntity search){
        if(getUserId() != Constant.SUPER_ADMIN){
            if (getUser().getRoleIdList() == null){
                return R.error("没有权限");
            }
            if (getUser().getRoleIdList().contains(Constant.MXYHJS_ID)){
                search.setCreateUser(getUserId());
            }
        }
        List<NodeMenuVo> dataSourceTree = txrhDbSourceService.getTreeList(search);
        List<NodeMenuVo> yzmxtree = txrhMxYzmxService.getTreeList(search);
        dataSourceTree.addAll(yzmxtree);
        // 两个list 合并  list1.addAll(list2)  或者通过流合并
        return R.ok().put("items",dataSourceTree);
    }

    @GetMapping("/zhtreelist")
    @ApiOperation("获取组合模型tree接口模型数据")
    public R zhtreelist(TxrhMxZhmxEntity search){
        if(getUserId() != Constant.SUPER_ADMIN){
            if (getUser().getRoleIdList() == null){
                return R.error("没有权限");
            }
            if (getUser().getRoleIdList().contains(Constant.MXYHJS_ID)){
                search.setCreateUser(getUserId());
            }
        }
        List<NodeMenuVo> tree = txrhMxZhmxService.getTreeList(search);
        // 两个list 合并  list1.addAll(list2)  或者通过流合并
        return R.ok().put("items",tree);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("信息")
    public R info(@PathVariable("id") String id){
		TxrhMxZhmxEntity txrhMxZhmx = txrhMxZhmxService.getInfoById(id);

        return R.ok().put("item", txrhMxZhmx);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("保存")
    public R save(@RequestBody TxrhMxZhmxEntity txrhMxZhmx){
        if(getUserId() != Constant.SUPER_ADMIN){
            if (getUser().getRoleIdList() == null){
                return R.error("没有权限");
            }
            if (getUser().getRoleIdList().contains(Constant.MXYHJS_ID)){
                txrhMxZhmx.setCreateUser(getUserId());
            }
        }else { // 如果是超级管理员，创建的信息直接就能被人看到不需要在提交审核
            txrhMxZhmx.setShZt(2);
            txrhMxZhmx.setFbZt(1);
        }
		txrhMxZhmxService.save(txrhMxZhmx);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation("修改")
    public R update(@RequestBody TxrhMxZhmxEntity txrhMxZhmx){
		txrhMxZhmxService.updateById(txrhMxZhmx);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除")
    public R delete(@PathVariable("id") String id){
        txrhMxZhmxService.deleteZhmxById(id);
        return R.ok();
    }

}
