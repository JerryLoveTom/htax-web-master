package com.htax.modules.product.controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.common.utils.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.htax.modules.product.entity.HisOutProductEntity;
import com.htax.modules.product.service.HisOutProductService;
import com.htax.common.utils.PageUtils;
import com.htax.common.utils.R;



/**
 * 出货记录表
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-07-08 09:37:50
 */
@RestController
@RequestMapping("product/hisoutproduct")
public class HisOutProductController {
    @Autowired
    private HisOutProductService hisOutProductService;
    @Value("${htax.testing}")
    private String url;
    @GetMapping("/tofunc")
    public R toFunc() throws IOException {
        // String url = "http://127.0.0.1:9012/api/data";
        // String param =  "name=james";
        Map<String,String>map = new HashMap<>();
        map.put("id","1");
        JSONObject s = HttpUtils.postMap(url, map);
        System.out.println(s);
        return R.ok().put("reslut",s.get("result"));
    }

    private R getR() {
        String res = "调用pythong成功";
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        String content = "[\n" +
                "        ['牛奶', '面包'], ['面包', '尿不湿', '啤酒', '鸡翅'], ['面包', '尿不湿', '啤酒', '可乐'], \n" +
                "        ['面包', '尿不湿', '啤酒', '啤酒'], ['面包', '牛奶', '尿不湿', '可乐']\n" +
                "        ]";
        jsonObject.put("name",content);
        String str = jsonObject.toJSONString();
        Socket socket = null;
        List<TypePatternQuestions.Question>questions = new ArrayList<>();
        String HOST= "127.0.0.1";
        int PORT = 9012;
        try{
            // 初始化套接字，设置访问ip 和端口
            socket = new Socket(HOST,PORT);
            // 获取标准输出流对象
            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os);
            // 发送内容
            out.print(str);
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
            String tmp = null;
            StringBuffer sb = new StringBuffer();
            while ((tmp = br.readLine())!=null){
                sb.append(tmp);
            }
            res = sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (socket!=null) socket.close();
            }catch (IOException e){}
            return R.ok().put("res", res);
        }
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:hisoutproduct:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = hisOutProductService.queryPage(params);

        return R.ok().put("page", page);
    }
    @RequestMapping("/list/{current}/{limit}")
    @RequiresPermissions("product:hisoutproduct:list")
    public R list(@PathVariable("current") Long current
            ,@PathVariable("limit") Long limit
            ,HisOutProductEntity search){
        Page<HisOutProductEntity> page = hisOutProductService.queryPage(current, limit, search);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("product:hisoutproduct:info")
    public R info(@PathVariable("id") Long id){
		HisOutProductEntity hisOutProduct = hisOutProductService.getById(id);

        return R.ok().put("hisOutProduct", hisOutProduct);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:hisoutproduct:save")
    public R save(@RequestBody HisOutProductEntity hisOutProduct){
		hisOutProductService.save(hisOutProduct);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:hisoutproduct:update")
    public R update(@RequestBody HisOutProductEntity hisOutProduct){
		hisOutProductService.updateById(hisOutProduct);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:hisoutproduct:delete")
    public R delete(@RequestBody Long[] ids){
		hisOutProductService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
