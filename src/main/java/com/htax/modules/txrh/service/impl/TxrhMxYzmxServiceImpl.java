package com.htax.modules.txrh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htax.modules.txrh.dao.TxrhMxYzmxDao;
import com.htax.modules.txrh.entity.TxrhMxYzmxEntity;
import com.htax.modules.txrh.entity.TxrhYzmxCcsEntity;
import com.htax.modules.txrh.entity.TxrhYzmxRcsEntity;
import com.htax.modules.txrh.entity.vo.NodeMenuVo;
import com.htax.modules.txrh.entity.vo.SelectOptionVo;
import com.htax.modules.txrh.entity.vo.YzmxVo;
import com.htax.modules.txrh.service.TxrhMxYzmxService;
import com.htax.modules.txrh.service.TxrhYzmxCcsService;
import com.htax.modules.txrh.service.TxrhYzmxRcsService;
import com.htax.modules.txrh.utils.TreeUtils;
import dm.jdbc.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @BelongsProject: htax-web-master
 * @BelongsPackage: com.htax.modules.txrh.service.impl
 * @Author: ppzz
 * @CreateTime: 2022-07-15  16:29
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class TxrhMxYzmxServiceImpl  extends ServiceImpl<TxrhMxYzmxDao, TxrhMxYzmxEntity> implements TxrhMxYzmxService {
    @Autowired
    TxrhYzmxRcsService yzmxRcsService; // 入参
    @Autowired
    TxrhYzmxCcsService yzmxCcsService; // 出参
    @Override
    public Page<TxrhMxYzmxEntity> queryPages(Long current, Long limit, TxrhMxYzmxEntity search) {
        Page<TxrhMxYzmxEntity> page = new Page<>(current, limit);
        QueryWrapper<TxrhMxYzmxEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtil.isNotEmpty(search.getMxMc()), "mx_mc",search.getMxMc());
        queryWrapper.eq(search.getCreateUser()!=null, "create_user", search.getCreateUser());
        queryWrapper.eq(search.getShZt() != null,"sh_zt",search.getShZt());
        queryWrapper.eq(search.getFbZt() != null, "fb_zt",search.getFbZt());
        queryWrapper.eq("ml_jd",1); // 只查节点
        queryWrapper.orderByDesc("update_time");
        Page<TxrhMxYzmxEntity> pageList = baseMapper.selectPage(page, queryWrapper);
        return pageList;
    }

    @Override
    public Page<TxrhMxYzmxEntity> queryPage(Long current, Long limit, TxrhMxYzmxEntity search) {
        Page<TxrhMxYzmxEntity> page = new Page<>(current, limit);
        search.setMlJd("1");
        return baseMapper.queryPage(page, search);
    }

    // 保存原子模型及其关联参数
    @Override
    @Transactional
    public void saveRelated(YzmxVo entity) {
        TxrhMxYzmxEntity txrhMxYzmx= new TxrhMxYzmxEntity();
        BeanUtils.copyProperties(entity, txrhMxYzmx);
        this.save(txrhMxYzmx);
        if (txrhMxYzmx.getSfyCs() == 1 && entity.getYzmxCccsList() != null){
            List<TxrhYzmxCcsEntity> collect = entity.getYzmxCccsList().stream().map(item -> {
                item.setMxId(txrhMxYzmx.getId());
                return item;
            }).collect(Collectors.toList());
            yzmxCcsService.saveBatch(collect);
        }
        if (txrhMxYzmx.getSfyFhz() == 1 && entity.getYzmxRccsList() != null){
            List<TxrhYzmxRcsEntity> collect = entity.getYzmxRccsList().stream().map(item -> {
                item.setMxId(txrhMxYzmx.getId());
                return item;
            }).collect(Collectors.toList());
            yzmxRcsService.saveBatch(collect);
        }
    }

    // 按照原子模型的Id修改原子模型及其关联参数
    @Override
    @Transactional
    public int updateRelatedById(YzmxVo entity) {
        TxrhMxYzmxEntity txrhMxYzmx= new TxrhMxYzmxEntity();
        BeanUtils.copyProperties(entity, txrhMxYzmx);
        this.updateById(txrhMxYzmx);
        if (txrhMxYzmx.getSfyCs() == 1 && entity.getYzmxCccsList() != null){
            List<TxrhYzmxCcsEntity> existList = new ArrayList<>();
            List<TxrhYzmxCcsEntity> reexistList = new ArrayList<>();
            entity.getYzmxCccsList().stream().forEach(txrhYzmxCcsEntity -> {
                if (txrhYzmxCcsEntity.getId() != null){
                    existList.add(txrhYzmxCcsEntity);
                }else {
                    txrhYzmxCcsEntity.setMxId(txrhMxYzmx.getId());
                    reexistList.add(txrhYzmxCcsEntity);
                }
            });
            if (existList.size() > 0){
                // 先删除已经不在这个参数列表内的
                yzmxCcsService.remove(new QueryWrapper<TxrhYzmxCcsEntity>().notIn("id",existList.stream().map(TxrhYzmxCcsEntity::getId).collect(Collectors.toList())));
                yzmxCcsService.updateBatchById(existList);
            }
            if (reexistList.size() > 0){
                yzmxCcsService.saveBatch(reexistList);
            }
        }
        if (txrhMxYzmx.getSfyFhz() == 1 && entity.getYzmxRccsList() != null){
            List<TxrhYzmxRcsEntity> existList = new ArrayList<>();
            List<TxrhYzmxRcsEntity> reexistList = new ArrayList<>();
            entity.getYzmxRccsList().stream().forEach(txrhYzmxRcsEntity -> {
                if (txrhYzmxRcsEntity.getId() != null){
                    existList.add(txrhYzmxRcsEntity);
                }else {
                    txrhYzmxRcsEntity.setMxId(txrhMxYzmx.getId());
                    reexistList.add(txrhYzmxRcsEntity);
                }
            });
            if (existList.size() > 0){
                // 先删除已经不在这个参数列表内的
                yzmxRcsService.remove(new QueryWrapper<TxrhYzmxRcsEntity>().notIn("id",existList.stream().map(TxrhYzmxRcsEntity::getId).collect(Collectors.toList())));
                yzmxRcsService.updateBatchById(existList);
            }
            if (reexistList.size() > 0){
                yzmxRcsService.saveBatch(reexistList);
            }
        }
        return 0;
    }

    // 根据原子模型Id 获取原子模型及其参数信息
    @Override
    public YzmxVo getYzmxAndParamsById(Long id) {
        YzmxVo vo = new YzmxVo();
        TxrhMxYzmxEntity yzmxEntity = this.getById(id);
        BeanUtils.copyProperties(yzmxEntity,vo);
        List<TxrhYzmxRcsEntity> rcsList = yzmxRcsService.list(new QueryWrapper<TxrhYzmxRcsEntity>().eq("mx_id",id));// 入参
        List<TxrhYzmxCcsEntity> ccsList = yzmxCcsService.list(new QueryWrapper<TxrhYzmxCcsEntity>().eq("mx_id",id)); // 出参
        vo.setYzmxCccsList(ccsList);
        vo.setYzmxRccsList(rcsList);
        return vo;
    }

    // 获取目录结构的选择框
    @Override
    public List<SelectOptionVo> getMenuOptions(TxrhMxYzmxEntity search) {
        List<TxrhMxYzmxEntity> yzmxList = baseMapper.selectList(new QueryWrapper<TxrhMxYzmxEntity>()
                .eq("ml_jd",0)
                .eq(search.getCreateUser()!=null,"create_user",search.getCreateUser()));
        List<SelectOptionVo>list = new ArrayList<>();
        if (yzmxList.size() > 0){
            yzmxList.stream().forEach(i -> {
                SelectOptionVo vo = new SelectOptionVo();
                vo.setId(i.getId().toString());
                vo.setPid(i.getPid());
                vo.setLabel(i.getMxMc());
                vo.setValue(i.getId().toString());
                list.add(vo);
            });
        // List<SelectOptionVo>vos = TreeUtils.getTreeList("0",list);
        }
        return list;
    }

    @Override
    public List<NodeMenuVo> getTreeList(TxrhMxYzmxEntity search) {
        QueryWrapper<TxrhMxYzmxEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtil.isNotEmpty(search.getMxMc()), "mx_mc",search.getMxMc());
        queryWrapper.eq(search.getCreateUser()!=null, "create_user", search.getCreateUser());
        queryWrapper.eq(search.getShZt() != null,"sh_zt",search.getShZt());
        queryWrapper.eq(search.getFbZt() != null, "fb_zt",search.getFbZt());
        queryWrapper.orderByDesc("update_time");
        // List<TxrhMxYzmxEntity> yzmxList = baseMapper.selectList(queryWrapper);
        List<TxrhMxYzmxEntity> yzmxList = baseMapper.getMineAndPublicModel(search);
        List<NodeMenuVo>list = new ArrayList<>();
        if (yzmxList.size() > 0){
            yzmxList.stream().forEach(i -> {
                NodeMenuVo vo = new NodeMenuVo();
                vo.setId(i.getId().toString());
                vo.setPid(i.getPid());
                vo.setName(i.getMxMc());
                vo.setType("model");
                if ("0".equals(i.getPid())){
                    vo.setIco("el-icon-video-pause");
                    vo.setOpen(true);
                }else{
                    vo.setIco("el-icon-document");
                }
                list.add(vo);
            });
            List<NodeMenuVo>vos = TreeUtils.getTreeList("0",list);
            return vos;
        }
        return list;
    }
}
