package com.htax.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.*;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.strings.StringUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
*@description:Es工具类
*@author: wangJ
*@date: 2022/5/17
*/
@Component
public class ElasticSearchUtils {


    @Autowired
    JestClient jestClient;

    //本地测试使用
//    static JestClient jestClient;
//    static{
//        JestClientFactory factory = new JestClientFactory();
//        factory.setHttpClientConfig(new HttpClientConfig.Builder("http://127.0.0.1:9200").multiThreaded(true).build());
//        jestClient = factory.getObject();
//    }

//    public static void main(String[] args) {
//        ElasticSearchUtils esService = new ElasticSearchUtils();
//        System.out.println(esService.count("wj", "user", ""));
//    }

    /**
    *@description: 获取全部索引
    *@params: []
    *@return: void
    *@author: wangJ
    *@date: 2022/5/21
    */
    public JestResult indexList() {
        JestResult jestResult = null;
        try {
            Cat cat = new Cat.IndicesBuilder().build();
            jestResult = jestClient.execute(cat);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jestResult;
    }

        /**
     * 创建索引
     * @param indexName 索引名称
     */
    public  void createIndex(String indexName) {
        boolean indexExists = false;
        try {
            indexExists = jestClient.execute(new IndicesExists.Builder(indexName).build()).isSucceeded();
            if (indexExists) {
                //删除操作可添加索引类型 .type(indexType)
                jestClient.execute(new DeleteIndex.Builder(indexName).build());
            }
            jestClient.execute(new CreateIndex.Builder(indexName).build());
            System.out.println("===========");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引
     * @param indexName 索引名称
     * @return true/false
     */
    public boolean deleteIndex(String indexName){
        JestResult jr = null;
        boolean bool = false;
        try {
            jr = jestClient.execute(new DeleteIndex.Builder(indexName).build());
            bool = jr.isSucceeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 获取索引映射
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @return Mapping映射
     */
    public  String getIndexMapping(String indexName, String indexType){
        GetMapping getMapping = new GetMapping.Builder().addIndex(indexName).addType(indexType).build();
        JestResult jr = null;
        String string = "";
        try {
            jr = jestClient.execute(getMapping);
            string = jr.getJsonString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * 创建索引映射
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param source    索引映射
     * @return true/false
     */
    public boolean createIndexMapping(String indexName, String indexType, String source){
        PutMapping putMapping = new PutMapping.Builder(indexName, indexType, source).build();
        JestResult jr = null;
        boolean bool = false;
        try {
            jr = jestClient.execute(putMapping);
            bool = jr.isSucceeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 根据文档id查询文档
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param id        文档id
     * @return 文档
     */
    public Map getIndexDocById(String indexName, String indexType, String id){
        Get get = new Get.Builder(indexName, id).type(indexType).build();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JestResult result = jestClient.execute(get);
            JsonObject jsonObject =  result.getJsonObject().get("_source").getAsJsonObject();
            Gson gson = new Gson();
            map = gson.fromJson(jsonObject,map.getClass());
            //将索引id存入map集合
            map.put("id",id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 根据文档id查询文档
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param id        文档id
     * @return 文档
     */
    public JsonObject getIndexDocByIdForJson(String indexName, String indexType, String id){
        Get get = new Get.Builder(indexName, id).type(indexType).build();
        Map<String, Object> map = new HashMap<String, Object>();
        JsonObject jsonObject = null;
        try {
            JestResult result = jestClient.execute(get);
            jsonObject =  result.getJsonObject().get("_source").getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 根据文档id删除索引文档
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param id 文档唯一id
     */
    public boolean deleteIndexDoc(String indexName, String indexType, String id){
        DocumentResult dr = null;
        boolean bool = false;
        try {
            dr = jestClient.execute(new Delete.Builder(id).index(indexName).type(indexType).build());
            bool = dr.isSucceeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 根据文档id删除索引文档
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param search 检索式
     */
    public boolean deleteIndexDocBySearch(String indexName, String indexType, String search){
        DocumentResult dr = null;
        boolean bool = false;
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.queryStringQuery(search).defaultOperator(Operator.AND));
            Search sb = new Search.Builder(sourceBuilder.toString()).addIndex(indexName).addType(indexType).build();
            dr = jestClient.execute(new Delete.Builder(sb.toString()).index(indexName).type(indexType).build());
            bool = dr.isSucceeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 批量索引文档，（因为设置了唯一id，所以该方法执行时索引字段必须有id）,id存在更新，不存在添加
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param list      List集合对戏
     * @return true/false
     */
    public boolean upsertIndexDocBulk(String indexName, String indexType, List<Map<String, Object>> list) {
        Bulk.Builder bulk = new Bulk.Builder().defaultIndex(indexName).defaultType(indexType);
        for (Map<String, Object> map : list) {
            //设置文档唯一id值，id存在执行更新，不存在执行添加
            Index index = new Index.Builder(map).id(map.get("id").toString()).build();
            bulk.addAction(index);
        }
        BulkResult br = null;
        boolean boll = false;
        try {
            br = jestClient.execute(bulk.build());
            boll =  br.isSucceeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boll;
    }

    /**
     * 批量新增索引文档
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param list 集合
     * @return true/false
     */
    public boolean updateIndexDocBulk(String indexName, String indexType, List<Map<String, Object>> list) {
        Bulk.Builder bulk = new Bulk.Builder().defaultIndex(indexName).defaultType(indexType);
        for (Map<String, Object> map : list) {
            //如未设置索引唯一id值，则唯一id会默认生成，索引操作为添加操作
            Index index = new Index.Builder(map).build();
            bulk.addAction(index);
        }
        BulkResult br = null;
        boolean boll = false;
        try {
            br = jestClient.execute(bulk.build());
            boll =  br.isSucceeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boll;
    }

    /**
     * 单条更新索引文档
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param map map集合
     * @return true/false
     */
    public boolean updateIndexDoc(String indexName, String indexType, Map<String, Object> map){
        Index index = new Index.Builder(map).index(indexName).type(indexType).id(map.get("id").toString()).refresh(true).build();
        JestResult jr = null;
        boolean bool = false;
        try {
            jr = jestClient.execute(index);
            bool = jr.isSucceeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 单条更新索引文档
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param id 索引id
     * @param map map集合
     * @return true/false
     */
    public boolean updateIndexDoc(String indexName, String indexType,String id, Map<String, Object> map){
        Index index = new Index.Builder(map).index(indexName).type(indexType).id(id).refresh(true).build();
        JestResult jr = null;
        boolean bool = false;
        try {
            jr = jestClient.execute(index);
            bool = jr.isSucceeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 单条更新索引文档
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param jsonObject jsonObject
     * @return true/false
     */
    public boolean updateIndexDoc(String indexName, String indexType, JsonObject jsonObject){
        Index index = new Index.Builder(jsonObject).index(indexName).type(indexType).id(jsonObject.get("id").toString()).build();
        JestResult jr = null;
        boolean bool = false;
        try {
            jr = jestClient.execute(index);
            bool = jr.isSucceeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 单条更新索引文档
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param id 索引id
     * @param jsonObject jsonObject
     * @return true/false
     */
    public boolean updateIndexDoc(String indexName, String indexType,String id, JsonObject jsonObject){
        Index index = new Index.Builder(jsonObject).index(indexName).type(indexType).id(id).build();
        JestResult jr = null;
        boolean bool = false;
        try {
            jr = jestClient.execute(index);
            bool = jr.isSucceeded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 聚类示例
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param field 操作字段
     * @param facetsField 聚类字段
     */
    public void facetsSearch(String indexName, String indexType, String field, String facetsField){
        SumAggregationBuilder sumBuilder = AggregationBuilders.sum(field).field(facetsField);
        Search sb = new Search.Builder(sumBuilder.toString()).addIndex(indexName).addType(indexType).build();

        SearchResult result = null;
        try {
            result = jestClient.execute(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 统计文档总数
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param search    检索式
     * @return 文档总数
     */
    public Double count(String indexName, String indexType, String search) {
        Count count = new Count.Builder().addIndex(indexName).addType(indexType).query(search).build();
        CountResult cr = null;
        Double db = 0d;
        try {
            cr = jestClient.execute(count);
            db = cr.getCount();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return db;
    }

    /**
     * 返回文档唯一id，根据检索式
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param search 检索式
     * @return
     */
    public String getIndexDocIds(String indexName, String indexType, String search){
        //设置默认参检索引
        indexName = StringUtils.isBlank(indexName) ? "*" : indexName;
        //设置默认检索全部
        search = StringUtils.isBlank(search) ? "*" : search;
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //设置默认分词后AND连接，StringQuery支持通配符
        sourceBuilder.query(QueryBuilders.queryStringQuery(search).defaultOperator(Operator.AND));
        sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));

        Search sb = new Search.Builder(sourceBuilder.toString()).addIndex(indexName).addType(indexType).build();
        SearchResult result = null;
        try {
            result = jestClient.execute(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long totalCount = 0;
        StringBuilder sb1 = new StringBuilder();
        if (result != null && result.isSucceeded()) {
            //获取总记录数
            totalCount = result.getJsonObject().get("hits").getAsJsonObject().get("total").getAsJsonObject().get("value").getAsLong();
            if (totalCount > 0) {
                Map<String, Object> map = new HashMap<String, Object>();
                JsonArray jsonArray = result.getJsonObject().get("hits").getAsJsonObject().get("hits").getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    String id = jsonArray.get(i).getAsJsonObject().get("_id").toString();
                    sb1.append(id+",");
                }
            }
        }
        return StringUtils.isNotBlank(sb1.toString()) ? sb1.toString().substring(0,sb1.toString().length()-1) : "";
    }

    /**
     * 查询，根据检索式查询
     * @param indexName 索引名称
     * @param indexType 索引类型
     * @param search 检索式
     * @param sortField 排序字段
     * @return 返回List结果集
     */
    public List baseSearch(String indexName, String indexType, String search, String sortField) {
        //设置默认参检索引
        indexName = StringUtils.isBlank(indexName) ? "*" : indexName;
        //设置默认检索全部
        search = StringUtils.isBlank(search) ? "*" : search;
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //设置返回结果集大小
        sourceBuilder.size(30);
        //设置默认分词后AND连接，StringQuery支持通配符
        sourceBuilder.query(QueryBuilders.queryStringQuery(search).defaultOperator(Operator.AND));
        //设置排序规则
        if (StringUtils.isNotBlank(sortField)) {
            sourceBuilder.sort(new FieldSortBuilder(sortField).order(SortOrder.ASC));
        } else {
            sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        }
        Search sb = new Search.Builder(sourceBuilder.toString()).addIndex(indexName).addType(indexType).build();
        SearchResult result = null;
        try {
            result = jestClient.execute(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long totalCount = 0;
        List<Object> list = new ArrayList<Object>();
        if (result != null && result.isSucceeded()) {
            //获取总记录数
            totalCount = result.getJsonObject().get("hits").getAsJsonObject().get("total").getAsJsonObject().get("value").getAsLong();
            if (totalCount > 0) {
                Map<String, Object> map = new HashMap<String, Object>();
                JsonArray jsonArray = result.getJsonObject().get("hits").getAsJsonObject().get("hits").getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject().get("_source").getAsJsonObject();
                    Gson gson = new Gson();
                    map = gson.fromJson(jsonObject,map.getClass());
                    //将索引id存入map集合
                    String id = jsonArray.get(i).getAsJsonObject().get("_id").toString();
                    map.put("id",id);
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 更新es索引数据,索引中有联合索引，需调用该方法进行数据更新
     * @param map 集合
     * @param indexName 索引名称
     * @param indexType 索引类型
     */
    public boolean updateEsData(String indexName, String indexType, Map<String, Object> map, String[] fields, String[] index ) {
        //存储数据
        Map<String, Object> hmap = new HashMap<String, Object>();
        for (int j = 0; j < fields.length; j++) {
            if (fields[j].contains(":")) {
                //分割联合字段
                String[] sa = fields[j].split(":");
                StringBuffer sb = new StringBuffer();
                for (String st : sa) {
                    //联合字段，取值拼接
                    sb.append(map.get(st) + ";");
                }
                hmap.put(index[j], sb.toString());
            } else {
                hmap.put(index[j], map.get(fields[j]));
            }
        }
        //针对car_model索引 添加 年款style_year_suffix  默认数据全部符合规范2017格式
        if (indexName.equals("car_model")) {
            if (null != map.get("style_year")) {
                hmap.put("style_year_suffix", map.get("style_year").toString().substring(2, map.get("style_year").toString().length()));
            }
        }
        //调用es更新方法
        boolean bool = updateIndexDoc(indexName,indexType,hmap);
        return bool;
    }

}
