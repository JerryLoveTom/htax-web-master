package com.htax.modules.txrh.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.common.exception.HTAXException;
import com.htax.common.utils.FileUtil;
import com.htax.common.utils.ThreadPoolUtils;
import com.htax.common.utils.uuid.IdUtils;
import com.htax.common.webscoket.WebSocket;
import com.htax.common.webscoket.WsMessage;
import com.htax.common.webscoket.WsMessageCodeEnum;
import com.htax.modules.txrh.entity.*;
import com.htax.modules.txrh.entity.vo.NodeMenuVo;
import com.htax.modules.txrh.entity.vo.WorkFlowDataVo;
import com.htax.modules.txrh.entity.vo.WorkFlowSocketMsgVo;
import com.htax.modules.txrh.service.*;
import org.json.JSONArray;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.htax.modules.txrh.dao.TxrhMxZhmxDao;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service("txrhMxZhmxService")
public class TxrhMxZhmxServiceImpl extends ServiceImpl<TxrhMxZhmxDao, TxrhMxZhmxEntity> implements TxrhMxZhmxService {
    @Autowired
    private TxrhZhmxJdService zhmxJdService; // 节点
    @Autowired
    private TxrhZhmxLxService zhmxLxService;// 连线
    @Autowired
    private TxrhDbSourceService dbSourceService;// 数据源（另一种形式的原子模型）
    @Autowired
    private TxrhZhmxJdSjyService zhmxJdSjyService; // 工作流（组合模型）数据源节点服务
    @Autowired
    private TxrhYzmxCcsService yzmxCcsService; // 原子模型出参服务
    @Autowired
    private TxrhYzmxRcsService yzmxRcsService; // 原子模型入参服务
    @Autowired
    private TxrhDBService dbService;
    @Autowired
    private WebSocket webSocket;
    @Value("${htax.profile}")
    private String profile;
    // 带分页，带条件的列表查询
    @Override
    public Page<TxrhMxZhmxEntity>  queryPages(Long current, Long limit, TxrhMxZhmxEntity search) {
        Page<TxrhMxZhmxEntity> page = new Page<>(current,limit);
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
        List<TxrhZhmxLxEntity> lineList = zhmxLxService.list(new QueryWrapper<TxrhZhmxLxEntity>()
                .select("*,from_node as `from`,to_node as `to`")
                .eq("zhmx_id", id));
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
        entity.getLineList().stream().forEach(item -> {
            item.setZhmxId(entity.getZhmxId());
            item.setFromNode(item.getFrom());
            item.setToNode(item.getTo());
        });
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
    public boolean cloneFlow(String id, long userId) {
        // 1、首先查询工作流基本信息并赋值给新对象
        TxrhMxZhmxEntity zhmxInfo = this.getById(id);
        TxrhMxZhmxEntity info = new TxrhMxZhmxEntity();
        BeanUtils.copyProperties(zhmxInfo,info);
        info.setId(null); // 因为是新增，所以id要为空
        info.setFbZt(0); // 发布状态
        info.setShZt(0); // 审核状态
        info.setMxMc(info.getMxMc() + "-副本");
        info.setCreateUser(userId);
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
                oldLineList.stream().filter(item -> tempEntity.getId().equals(item.getFromNode())).forEach(item->item.setFromNode(newNodeId));
                oldLineList.stream().filter(item -> tempEntity.getId().equals(item.getToNode())).forEach(item->item.setToNode(newNodeId));
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

    /**
     * 通过id执行工作流
     * ticket 票据
     * */
    @Override
    public void runWorkFlow(String id , String ticket) throws InterruptedException {
        // 首先获取改工作流中的所有节点及节点连线
        // TxrhMxZhmxEntity info = this.getById(id); // 获取工作流基础信息
        List<TxrhZhmxJdEntity> nodeList = zhmxJdService.list(new QueryWrapper<TxrhZhmxJdEntity>().eq("zhmx_id", id)); // 获取节点列表
        List<TxrhZhmxLxEntity> lineList = zhmxLxService.list(new QueryWrapper<TxrhZhmxLxEntity>().eq("zhmx_id", id)); // 获取连线列表
        List<TxrhZhmxJdEntity> startNodes = getStartNodes(nodeList, lineList);
        System.out.println( startNodes);
        // 记录已经被执行过的节点
        List<String> successIds = new ArrayList<>();
        // 执行方案中的节点
        execNode(nodeList, lineList, startNodes, successIds, id, true, true, ticket);
    }
    /**
     * 执行节点
     * nodeList: 工作流中所有的节点集合
     * lineList： 工作流中所有的连线集合
     * startNodes：需要启动的节点集合
     * successIds： 已经执行完毕的节点id
     * workFlowId: 工作流id(组合模型的id)
     * ticket : 本次执行的票据，是生成文件的依据
     * */
    private void execNode(List<TxrhZhmxJdEntity> nodeList, List<TxrhZhmxLxEntity> lineList, List<TxrhZhmxJdEntity> startNodes
            ,List<String>successIds,String workFlowId, boolean isCustom, boolean sendMsg, String ticket) throws InterruptedException {
        // 如果节点集合为空，则不需要执行
        if (nodeList == null || nodeList.size() == 0) return;
        // websocket通知前端要执行算法了
        if (sendMsg){
            for (TxrhZhmxJdEntity vo : startNodes) {
                String nodeId = vo.getId();
                for (TxrhZhmxJdEntity node: nodeList) {
                    if (nodeId.equals(node.getId())){
                        vo.setState("running");
                        node.setState("running");
                    }
                }
            }
            sendMsgToWeb(nodeList);
        }
        AtomicBoolean execNext = new AtomicBoolean(true);// 保证一致性
        CountDownLatch countDownLatch = new CountDownLatch(startNodes.size()); //
        for (TxrhZhmxJdEntity node : startNodes){
            nodeExecThread(nodeList, successIds, workFlowId, ticket, countDownLatch, node, sendMsg);
        }
        countDownLatch.await(); // 如果计数不为0，一直等待
        sendMsgToWeb(nodeList); // 给前端发送websocket消息，通知其更新
        List<TxrhZhmxJdEntity> nextExecNodes = getNextExecNodes(startNodes, nodeList, lineList, successIds);
        if (nextExecNodes.size() > 0){ // 如果待执行节点集合大于0，则继续递归
            System.out.println(" 执行递归=+++++ "+ startNodes);
            execNode(nodeList, lineList, nextExecNodes, successIds, workFlowId, isCustom, sendMsg, ticket);
        }
    }

    // 线程池调度节点运行节点
    private void nodeExecThread(List<TxrhZhmxJdEntity> nodeList, List<String> successIds, String workFlowId, String ticket
            , CountDownLatch countDownLatch, TxrhZhmxJdEntity node, boolean sendMsg) {
        ThreadPoolUtils.execute(()->{
            try{
                String id = node.getId();
                Map<String,Object> res = null;
                System.out.println("开始执行 ： " + node.getName()); // 输出的是节点名称
                long startTime = System.currentTimeMillis();
                System.out.println("nodeMold:" + node.getMold());
                if ("0".equals(node.getMold())){// 0 数据源  1.模型
                    getAndWriteSourceData(node, ticket, workFlowId);
                    successIds.add(node.getId());
                    System.out.println("数据源节点名称：" + node.getName());
                }else if("1".equals(node.getMold())){
                    // 所有模型都必须有输入节点，所以只要是模型，就是子级节点
                    System.out.println("模型节点名称：" + node.getName());
                    execModel(node); // 执行模型节点
                }
                if (sendMsg){ // 如果需要给前端发送消息
                    String nodeId = node.getId();
                    nodeList.forEach(item -> {
                        if (nodeId.equals(item.getId())){
                            node.setState("success");
                            item.setState("success");
                        }
                    });
                    sendMsgToWeb(nodeList);
                }
            }catch(Exception e){
                String nodeId = node.getId();
                nodeList.forEach(item -> {
                    if (nodeId.equals(item.getId())){
                        node.setState("error");
                        item.setState("error");
                        node.setExecStatus(false);
                        item.setExecStatus(false);

                    }
                });
                throw new HTAXException("执行节点错误");
            }finally {
                // 开始调用模型算法时，成功执行和失败都需要返回消息给前端，需要将消息封装在此处
                countDownLatch.countDown(); // 线程执行完毕，计数器减1 sync.releaseShared(1);
            }
        });
    }
    /**
     * 执行模型节点
     * node：需要被执行的节点，该节点一定是模型
     * */
    private void execModel(TxrhZhmxJdEntity node){
        // 1.首先获取当前节点的入参
        List<TxrhYzmxRcsEntity> inParams = yzmxRcsService.list(new QueryWrapper<TxrhYzmxRcsEntity>().eq("id", node.getYzmxId()));
        // 2.通过入参，查找上节点出参

        // 3.形成 into_node.getId() + .json文件

        // 4.调用将文件地址及算法id 作为参数传递过去

        // 5.执行成功后，python形成out文件，

        // 6.通知前端执行成功
    }
    /**
     * 查询下联节点，并查看下联节点是否存在有未执行的上级节点，如果有，则需要存储
     * execNodes: 刚执行完毕的节点
     * nodeList: 所有的节点
     * lineList: 所有的连线
     * successIds: 所有执行成功的节点id
     * */
    private List<TxrhZhmxJdEntity> getNextExecNodes(List<TxrhZhmxJdEntity> execNodes, List<TxrhZhmxJdEntity> nodeList
            , List<TxrhZhmxLxEntity> lineList, List<String>successIds){
        // 1.现将执行成功的节点id 存放至成功id集合
        List<String> execNodeIds = execNodes.stream().map(TxrhZhmxJdEntity::getId).collect(Collectors.toList());
        successIds.addAll(execNodeIds);
        // 2.查询下联节点
        // 2.1 首先从连线集合中查询出所有的下联节点id
        List<String> toNodeIds = lineList.stream().filter(item -> execNodeIds.contains(item.getFromNode())).map(TxrhZhmxLxEntity::getToNode).distinct().collect(Collectors.toList());
        // 2.1 通过节点，查找出其上联尚未执行的节点（一直找到根节点）,最后将未执行的上联节点（最上层的）和当前不存在未执行的上联节点的节点去执行
        List<TxrhZhmxJdEntity> unexecNodes = new ArrayList<>();
        List<TxrhZhmxJdEntity> unexec = unexecFromNode(toNodeIds, nodeList,lineList, successIds, unexecNodes);

        return unexec;
    }
    /**
     * 判断当前节点是否有尚未执行的上联节点
     * toNodeIds : 当前需要判断是否存在上联未执行的节点id集合
     * nodeList：工作流的所有节点集合
     * lineList： 工作流所有的连线集合
     * successIds： 已经执行成功的所有节点的id
     * unexecNodes: 尚未执行的点的集合
     * */
    private List<TxrhZhmxJdEntity> unexecFromNode(List<String> toNodeIds, List<TxrhZhmxJdEntity> nodeList
            , List<TxrhZhmxLxEntity> lineList, List<String>successIds, List<TxrhZhmxJdEntity> unexecNodes){
        if (toNodeIds.size() > 0){
            // 找出还未执行的节点id：通过下联节点找出上联节点，并且这个上联节点还需要是未在执行成功的节点id中未出现过的。
            for (int i = 0; i < toNodeIds.size(); i++) {
                // 先找出当前节点的父节点:是toNodeIds的上联节点并且不包含在成功执行集合中
                List<String> fromNodeIds = lineList.stream().filter(item -> toNodeIds.contains(item.getToNode()) && !successIds.contains(item.getFromNode())).map(TxrhZhmxLxEntity::getFromNode).collect(Collectors.toList());
                if (fromNodeIds.size() > 0){
                    unexecFromNode(fromNodeIds, nodeList, lineList, successIds, unexecNodes);
                    //System.out.println("还未执行的父节点：" + unexecNodes);
                }else{ // 没有还未执行的父节点
                    int finalI = i;
                    List<TxrhZhmxJdEntity> unexec = nodeList.stream().filter(item -> toNodeIds.get(finalI).equals(item.getId())).collect(Collectors.toList());
                    unexecNodes.addAll(unexec);
                    //System.out.println("还未执行的节点：" + unexecNodes);
                }
            }
            // 通过还未执行的节点Id 找出还未执行的节点
        }
        List<TxrhZhmxJdEntity> unexec = unexecNodes.stream().distinct().collect(Collectors.toList());
        return unexec;
    }
    // 查询数据源表中数据并生成json文件
    private void getAndWriteSourceData(TxrhZhmxJdEntity node, String ticket, String workFlowId){
        // 如果是数据源，则直接查库并且将匹配的参数输出成文件进行保存
        // 1.确定当前节点绑定的表
        TxrhZhmxJdSjyEntity jdSjyInfo = zhmxJdSjyService.getOne(new QueryWrapper<TxrhZhmxJdSjyEntity>().eq("jd_id", node.getId()).last("limit 1"));
        // 2.需要查询的数据源 txrh_db_source
        TxrhDbSourceEntity dbInfo = dbSourceService.getById(node.getYzmxId());
        // 3.通过数据源及表名称，查询结果
        System.out.println("数据源id:"+dbInfo.getId());
        System.out.println("表id:"+jdSjyInfo.getTableName());
        String sql = "select * from "+ jdSjyInfo.getTableName();
        JSONArray items = dbService.getDataBySourceAndTable(dbInfo, sql);
        // 将结果输出到文件中
        String filePath = getSaveFilePath(node.getId(), "out", workFlowId, ticket);
        // 在文件中写入数据
        try {
            FileUtil.writeDataToFile(filePath, "jsonStr="+items.toString());
            node.setState("success");
            node.setExecStatus(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 生成节点文件输入参数或输出参数的文件路径
     * 创建文件 文件名为凭据_nodeId.json
     * 路径构成：profile/workflow/ticket/out_nodeid.json
     * nodeId : 节点Id
     * inOrOut : 输入或输出  值有且仅可以是 in  out
     * workFlowId : 工作流ID
     * ticket : 交易清单编号
     * */
    private String getSaveFilePath(String nodeId, String inOrOut, String workFlowId, String ticket){
        String fileName =  inOrOut + "_" + nodeId+ ".json";
        String halfPath =  File.separator + "workflow"+ File.separator + workFlowId + File.separator + ticket + File.separator;
        String filePath = profile + halfPath + fileName;
        return filePath;
    }
    /**
     * websocket通知前端
     * startNodes ：正在执行或刚执行完毕的节点集合
     * */
    private void sendMsgToWeb( List<TxrhZhmxJdEntity> nodeList){
        try {
            WorkFlowSocketMsgVo vo = new WorkFlowSocketMsgVo();
            vo.setZhmxJdList(nodeList);
            webSocket.sendMessage(new WsMessage(WsMessageCodeEnum.WORKFLOWSTATUS.getCode(), JSON.toJSONString(vo)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 找出工作流模板的起点
     * nodeList ： 工作流中的所有节点集合
     * lineList ： 工作流中所有的连线集合
     * */
    private List<TxrhZhmxJdEntity> getStartNodes( List<TxrhZhmxJdEntity> nodeList, List<TxrhZhmxLxEntity> lineList){
        // 1 找出起点
        // 1.1 找出所有被连线指向过的节点。
        List<String> toNodeIds = lineList.stream().map(TxrhZhmxLxEntity::getToNode).distinct().collect(Collectors.toList());
        List<String> allNodeIds = nodeList.stream().map(TxrhZhmxJdEntity::getId).collect(Collectors.toList());
        // 1.2 求差集
        Map<String, String> toNodeIdsMap = toNodeIds.parallelStream().collect(Collectors.toMap(Function.identity(), Function.identity(), (oldData, newData) -> newData));
        List<String> startIds = allNodeIds.parallelStream().filter(str -> { return !toNodeIdsMap.containsKey(str); }).collect(Collectors.toList());
        List<TxrhZhmxJdEntity> startNodes = nodeList.stream().filter(item -> startIds.contains(item.getId())).collect(Collectors.toList());
        return startNodes;
    }

    /**
     * 获取当前节点的上级节点
     * toId : 当前节点
     * */
    @Override
    public List<TxrhZhmxJdEntity> getFromJdByToId(String toId) {
        // 通过连线，获取所有的上级节点
        List<TxrhZhmxLxEntity> lineList = zhmxLxService.list(new QueryWrapper<TxrhZhmxLxEntity>().eq("`to_node`", toId));
        if (lineList.size()>0){
            List<String> fromIds = lineList.stream().map(TxrhZhmxLxEntity::getFromNode).collect(Collectors.toList());
            // 通过上级节点id ,获取所有节点信息
            List<TxrhZhmxJdEntity> nodeList = zhmxJdService.list(new QueryWrapper<TxrhZhmxJdEntity>().in("id", fromIds));
            return nodeList;
        }
        return null;
    }

}
