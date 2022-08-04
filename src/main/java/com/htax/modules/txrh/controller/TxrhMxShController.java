package com.htax.modules.txrh.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.common.utils.Constant;
import com.htax.common.utils.R;
import com.htax.modules.sys.controller.AbstractController;
import com.htax.modules.txrh.entity.TxrhMxYzmxEntity;
import com.htax.modules.txrh.service.TxrhMxYzmxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @BelongsProject: htax-web-master
 * @BelongsPackage: com.htax.modules.txrh.controller
 * @Author: ppzz
 * @CreateTime: 2022-08-02  14:51
 * @Description: TODO: 模型审核
 * @Version: 1.0
 */
@RestController
@RequestMapping("txrh/model/review")
@Api("模型审核")
public class TxrhMxShController extends AbstractController {
    @Autowired
    private TxrhMxYzmxService txrhMxYzmxService;
    /**
     * 列表
     */
    @GetMapping("/list/{current}/{limit}")
    @ApiOperation("原子模型审核列表")
    public R list(@ApiParam(name = "current",value = "当前页码") @PathVariable Long current
            , @ApiParam(name = "limit",value = "每页记录数")@PathVariable Long limit
            , TxrhMxYzmxEntity search){
        if(getUserId() != Constant.SUPER_ADMIN){
            if (getUser().getRoleIdList() == null){
                return R.error("没有权限");
            }
            if (getUser().getRoleIdList().contains(Constant.MXYHJS_ID)){
                search.setCreateUser(getUserId());
            }
        }
        Page<TxrhMxYzmxEntity> page = txrhMxYzmxService.queryPages(current, limit, search);
        return R.ok().put("page", page);
    }
}
