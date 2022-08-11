package com.htax.modules.txrh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.common.utils.uuid.IdUtils;
import com.htax.modules.txrh.entity.TxrhZhmxJdEntity;
import com.htax.modules.txrh.entity.TxrhZhmxLxEntity;
import com.htax.modules.txrh.entity.vo.NodeMenuVo;
import com.htax.modules.txrh.entity.vo.WorkFlowDataVo;
import com.htax.modules.txrh.service.TxrhZhmxJdService;
import com.htax.modules.txrh.service.TxrhZhmxLxService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.htax.modules.txrh.dao.TxrhMxZhmxDao;
import com.htax.modules.txrh.entity.TxrhMxZhmxEntity;
import com.htax.modules.txrh.service.TxrhMxZhmxService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service("txrhMxZhmxService")
public class TxrhMxZhmxServiceImpl extends ServiceImpl<TxrhMxZhmxDao, TxrhMxZhmxEntity> implements TxrhMxZhmxService {
    @Autowired
    private TxrhZhmxJdService zhmxJdService; // 节点
    @Autowired
    private TxrhZhmxLxService zhmxLxService;// 连线

    // 带分页，带条件的列表查询
    @Override
    public Page<TxrhMxZhmxEntity>  queryPages(Long current, Long limit, TxrhMxZhmxEntity search) {
        Page<TxrhMxZhmxEntity> page = new Page<>(current,limit);
       /* QueryWrapper<TxrhMxZhmxEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtil.isNotEmpty(search.getMxMc()), "mx_mc", search.getMxMc());
        queryWrapper.eq(search.getFbZt() != null, "fb_zt",search.getFbZt());
        queryWrapper.eq(search.getShZt() != null, "sh_zt",search.getShZt());
        queryWrapper.eq(search.getCreateUser()!=null, "create_user", search.getCreateUser());
        queryWrapper.orderByDesc("update_time");*/
        return baseMapper.queryPages(page, search);
    }
    // 获取组合模型tree接口模型数据
    @Override
    public List<NodeMenuVo> getTreeList(TxrhMxZhmxEntity search) {
        // TODO: 代码需要优化
        // 考虑几个问题：1 如果是管理员登陆，看到的是公开的模型，如果是本人登陆既能看到自己的，也能看到公开的
        // 管理员只能去审核模型
        List<NodeMenuVo>list = new ArrayList<>(2);
        List<TxrhMxZhmxEntity> resultList = baseMapper.getMineAndPublic(search);
        NodeMenuVo myModel = new NodeMenuVo();
        myModel.setId("1-1");
        myModel.setLabel("我的工作流");
        NodeMenuVo pubModel = new NodeMenuVo();
        pubModel.setId("1-2");
        pubModel.setLabel("公开工作流");
        if (resultList.size() > 0){
            List<NodeMenuVo>tempList = new ArrayList<>();
            List<NodeMenuVo>pubList = new ArrayList<>();
            if (search.getCreateUser() != null){
                resultList.stream().filter(i -> search.getCreateUser().equals(i.getCreateUser())).forEach(i -> {
                    NodeMenuVo vo = new NodeMenuVo();
                    vo.setId(i.getId());
                    vo.setPid(i.getPid());
                    vo.setLabel(i.getMxMc());
                    vo.setValue(i.getId());
                    tempList.add(vo);
                });
                myModel.setChildren(tempList);
                resultList.stream().filter(i -> !search.getCreateUser().equals(i.getCreateUser())).forEach(i -> {
                    NodeMenuVo vo = new NodeMenuVo();
                    vo.setId(i.getId());
                    vo.setPid(i.getPid());
                    vo.setLabel(i.getMxMc());
                    vo.setValue(i.getId());
                    pubList.add(vo);
                });
                pubModel.setChildren(pubList);
            }
            if (search.getCreateUser() == null){
                resultList.stream().forEach(i -> {
                    NodeMenuVo vo = new NodeMenuVo();
                    vo.setId(i.getId());
                    vo.setPid(i.getPid());
                    vo.setLabel(i.getMxMc());
                    vo.setValue(i.getId());
                    pubList.add(vo);
                });
                pubModel.setChildren(pubList);
            }
        }
        list.add(myModel);
        list.add(pubModel);
        return list;
    }
    @Override
    public TxrhMxZhmxEntity getInfoById(String id) {
        return baseMapper.getInfoById(id);
    }

    // 通过组合模型id获取模型、节点及连线信息
    @Override
    public WorkFlowDataVo getFlowDataById(String id) {
        WorkFlowDataVo vo = new WorkFlowDataVo();
        // 1 获取组合模型基础信息
        TxrhMxZhmxEntity info = this.getById(id);
        vo.setName(info.getMxMc());
        // 2 获取组合模型节点信息
        List<TxrhZhmxJdEntity> nodeList = zhmxJdService.list(new QueryWrapper<TxrhZhmxJdEntity>().eq("zhmx_id", id));
        vo.setNodeList(nodeList);
        // 3 获取组合模型连线信息
        List<TxrhZhmxLxEntity> lineList = zhmxLxService.list(new QueryWrapper<TxrhZhmxLxEntity>().eq("zhmx_id", id));
        vo.setLineList(lineList);
        return vo;
    }
    //保存模型流程信息
    @Override
    @Transactional
    public int saveFlowData(WorkFlowDataVo entity) {
        // 删除节点
        zhmxJdService.remove(new QueryWrapper<TxrhZhmxJdEntity>().eq("zhmx_id", entity.getZhmxId()));
        // 删除连线
        zhmxLxService.remove(new QueryWrapper<TxrhZhmxLxEntity>().eq("zhmx_id", entity.getZhmxId()));
        // 保存节点
        entity.getNodeList().stream().forEach(item -> item.setZhmxId(entity.getZhmxId()));
        zhmxJdService.saveBatch(entity.getNodeList());
        // 保存连线
        entity.getLineList().stream().forEach(item -> item.setZhmxId(entity.getZhmxId()));
        zhmxLxService.saveBatch(entity.getLineList());
        return 0;
    }

    // 删除组合模型及其关联表相关数据
    @Override
    @Transactional
    public boolean deleteZhmxById(String id) {
        // TODO： 后期可能会有变动，比如参数配置信息等
        // 删除工作流节点信息
        zhmxJdService.remove(new QueryWrapper<TxrhZhmxJdEntity>().eq("zhmx_id", id));
        // 删除工作流连线信息
        zhmxLxService.remove(new QueryWrapper<TxrhZhmxLxEntity>().eq("zhmx_id", id));
        // 删除工作流基础信息（组合模型信息）
        return this.removeById(id);
    }

    // 克隆模型信息
    @Override
    public boolean cloneFlow(String id) {
        // 1、首先查询工作流基本信息并赋值给新对象
        TxrhMxZhmxEntity zhmxInfo = this.getById(id);
        TxrhMxZhmxEntity info = new TxrhMxZhmxEntity();
        BeanUtils.copyProperties(zhmxInfo,info);
        info.setId(null); // 因为是新增，所以id要为空
        info.setFbZt(0); // 发布状态
        info.setShZt(0); // 审核状态
        info.setMxMc(info.getMxMc() + "-副本");
        this.save(info);// 保存信息，并返回info的Id
        // 2、查询节点信息并赋值给新list
        List<TxrhZhmxJdEntity> oldNodeList = zhmxJdService.list(new QueryWrapper<TxrhZhmxJdEntity>().eq("zhmx_id", id));
        // 3、查询连线信息并赋值给新list
        List<TxrhZhmxLxEntity> oldLineList = zhmxLxService.list(new QueryWrapper<TxrhZhmxLxEntity>().eq("zhmx_id", id));
        if (oldNodeList.size() > 0){
            // 实例化两个列表用来存储新的节点和连线信息
            List<TxrhZhmxJdEntity> nodeList = new ArrayList<>(oldNodeList.size());
            for (int i = 0; i < oldNodeList.size(); i++) {
                TxrhZhmxJdEntity tempEntity = oldNodeList.get(i);
                TxrhZhmxJdEntity node = new TxrhZhmxJdEntity();
                BeanUtils.copyProperties(tempEntity, node);
                // 节点的新id
                String newNodeId = IdUtils.simpleUUID();
                node.setId(newNodeId);
                node.setZhmxId(info.getId());
                nodeList.add(node);
                // 修改连线
                oldLineList.stream().filter(item -> tempEntity.getId().equals(item.getFrom())).forEach(item->item.setFrom(newNodeId));
                oldLineList.stream().filter(item -> tempEntity.getId().equals(item.getTo())).forEach(item->item.setTo(newNodeId));
            }
            // 修改连线中的工作流Id
            List<TxrhZhmxLxEntity> lineList = oldLineList.stream().map(item -> {
                item.setZhmxId(info.getId());
                item.setId(IdUtils.simpleUUID());
                return item;
            }).collect(Collectors.toList());
            zhmxJdService.saveBatch(nodeList);
            zhmxLxService.saveBatch(lineList);
        }
        return true;
    }

    // 通过id执行工作流
    @Override
    public void runWorkFlow(String id) {
        // 1.首先获取改工作流中的所有节点及节点连线
        TxrhMxZhmxEntity info = this.getById(id); // 获取工作流基础信息
        List<TxrhZhmxJdEntity> nodeList = zhmxJdService.list(new QueryWrapper<TxrhZhmxJdEntity>().eq("zhmx_id", id)); // 获取节点列表
        List<TxrhZhmxLxEntity> lineList = zhmxLxService.list(new QueryWrapper<TxrhZhmxLxEntity>().eq("zhmx_id", id)); // 获取连线列表
        // 2.找出起点

    }

    // 获取当前节点的上级节点
    @Override
    public List<TxrhZhmxJdEntity> getFromJdByToId(String toId) {
        // 通过连线，获取所有的上级节点
        List<TxrhZhmxLxEntity> lineList = zhmxLxService.list(new QueryWrapper<TxrhZhmxLxEntity>().eq("`to`", toId));
        if (lineList.size()>0){
            List<String> fromIds = lineList.stream().map(TxrhZhmxLxEntity::getFrom).collect(Collectors.toList());
            // 通过上级节点id ,获取所有节点信息
            List<TxrhZhmxJdEntity> nodeList = zhmxJdService.list(new QueryWrapper<TxrhZhmxJdEntity>().in("id", fromIds));
            return nodeList;
        }
        return null;
    }

}
