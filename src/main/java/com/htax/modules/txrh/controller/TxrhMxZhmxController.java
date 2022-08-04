package com.htax.modules.txrh.controller;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.common.utils.Constant;
import com.htax.modules.sys.controller.AbstractController;
import com.htax.modules.txrh.entity.TxrhMxYzmxEntity;
import com.htax.modules.txrh.entity.vo.NodeMenuVo;
import com.htax.modules.txrh.service.TxrhMxYzmxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    private TxrhMxZhmxService txrhMxZhmxService;
    @Autowired
    private TxrhMxYzmxService txrhMxYzmxService;
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
     * 列表
     */
    @GetMapping("/list/{current}/{limit}")
    @ApiOperation("列表")
    public R list(@ApiParam(name = "current",value = "当前页码") @PathVariable Long current
            , @ApiParam(name = "limit",value = "每页记录数")@PathVariable Long limit
            ,TxrhMxZhmxEntity search){
        Page<TxrhMxZhmxEntity> page = txrhMxZhmxService.queryPage(current, limit, search);

        return R.ok().put("page", page);
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
    @DeleteMapping("/delete")
    @ApiOperation("删除")
    public R delete(@RequestBody String[] ids){
		txrhMxZhmxService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
