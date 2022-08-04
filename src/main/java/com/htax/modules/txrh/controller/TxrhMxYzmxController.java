package com.htax.modules.txrh.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.common.utils.Constant;
import com.htax.common.utils.uuid.UUID;
import com.htax.modules.sys.controller.AbstractController;
import com.htax.modules.txrh.entity.TxrhMxYzmxEntity;
import com.htax.modules.txrh.entity.vo.NodeMenuVo;
import com.htax.modules.txrh.entity.vo.SelectOptionVo;
import com.htax.modules.txrh.entity.vo.YzmxVo;
import com.htax.modules.txrh.service.TxrhMxYzmxService;
import com.htax.modules.txrh.utils.FuncPyModelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.htax.common.utils.R;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


/**
 * 原子模型基础表
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-07-13 17:42:03
 */
@RestController
@RequestMapping("txrh/model/yzmx")
@Api("原子模型基础表")
public class TxrhMxYzmxController  extends AbstractController {
    @Autowired
    private TxrhMxYzmxService txrhMxYzmxService;
    @Value("${htax.testing}")
    private String url;

    @GetMapping("/testxml")
    @ApiOperation("测试")
    public R xml() throws IOException, XPathExpressionException {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        //读取xml文件,文件位于src目录下
        InputStream in = TxrhMxYzmxController.class.getResourceAsStream("/computer.xml");
        InputSource source = new InputSource(in);
        //开始解析
        String str = (String)xPath.evaluate("/computerList/computer[name='戴尔']/product", source);
        //打印结果
        System.out.println("xml获取的数据："+str);
        return R.ok();
    }

    /**
     * 测试模型
     */
    @PostMapping("/test")
    @ApiOperation("测试")
    public R save(@RequestBody Map<String,Object>arg) throws IOException {
        FuncPyModelUtils modelUtils = new FuncPyModelUtils();
        Map<String,Object>map = new HashMap<>();
        map.put("userId",getUserId());
        map.put("ticket", UUID.fastUUID().toString(true));
        map.put("id",arg.get("id"));
        map.put("params",arg);
        JSONObject func = modelUtils.func(url, map);
        if (!"200".equals(func.get("code").toString())){
            return R.error(func.get("msg").toString());
        }
        return R.ok().put("result",func.get("result"));
    }

    @GetMapping("/treelist")
    @ApiOperation("获取tree接口模型数据")
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
        return R.ok().put("items",tree);
    }
    /**
     * 列表
     */
    @GetMapping("/list/{current}/{limit}")
    @ApiOperation("列表")
    public R list(@ApiParam(name = "current",value = "当前页码") @PathVariable Long current
            , @ApiParam(name = "limit",value = "每页记录数")@PathVariable Long limit
            ,TxrhMxYzmxEntity search){
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

    @GetMapping("/menu/option")
    @ApiOperation("列表")
    public R menuOptions(){
        TxrhMxYzmxEntity search = new TxrhMxYzmxEntity();
        if(getUserId() != Constant.SUPER_ADMIN) {
            if (getUser().getRoleIdList().contains(Constant.MXYHJS_ID)) {
                search.setCreateUser(getUserId());
            }
        }
        List<SelectOptionVo> options = txrhMxYzmxService.getMenuOptions(search);

        return R.ok().put("items",options);
    }
    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("信息")
    public R info(@PathVariable("id") Long id){
		YzmxVo item = txrhMxYzmxService.getYzmxAndParamsById(id);

        return R.ok().put("item", item);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("保存")
    public R save(@RequestBody YzmxVo entity){
        if(getUserId() != Constant.SUPER_ADMIN){
            if (getUser().getRoleIdList() == null){
                return R.error("没有权限");
            }
            if (getUser().getRoleIdList().contains(Constant.MXYHJS_ID)){
                entity.setCreateUser(getUserId());
            }
        }
		txrhMxYzmxService.saveRelated(entity);

        return R.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation("修改")
    public R update(@RequestBody YzmxVo entity){
		txrhMxYzmxService.updateRelatedById(entity);

        return R.ok();
    }
    @PutMapping("/submitreview")
    @ApiOperation("提交审核")
    public R submitReview(@RequestBody YzmxVo entity){
        TxrhMxYzmxEntity info = new TxrhMxYzmxEntity();
        BeanUtils.copyProperties(entity,info);
        txrhMxYzmxService.updateById(info);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除")
    public R delete(@RequestBody Long[] ids){
		txrhMxYzmxService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
