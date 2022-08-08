package com.htax.modules.txrh.controller;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.common.utils.Constant;
import com.htax.modules.sys.controller.AbstractController;
import com.htax.modules.txrh.entity.TxrhMxYzmxEntity;
import com.htax.modules.txrh.entity.vo.NodeMenuVo;
import com.htax.modules.txrh.entity.vo.WorkFlowDataVo;
import com.htax.modules.txrh.entity.vo.YzmxVo;
import com.htax.modules.txrh.service.TxrhMxYzmxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.htax.modules.txrh.entity.TxrhMxZhmxEntity;
import com.htax.modules.txrh.service.TxrhMxZhmxService;
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
        List<NodeMenuVo> tree = txrhMxYzmxService.getTreeList(search);
        // 两个list 合并  list1.addAll(list2)  或者通过流合并
        return R.ok().put("items",tree);
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
