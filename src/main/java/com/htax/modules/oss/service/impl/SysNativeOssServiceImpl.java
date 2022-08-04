package com.htax.modules.oss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htax.modules.oss.dao.SysNativeOssDao;
import com.htax.modules.oss.entity.SysNativeOssEntity;
import com.htax.modules.oss.service.SysNativeOssService;
import com.htax.modules.oss.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service("sysNativeOssService")
public class SysNativeOssServiceImpl extends ServiceImpl<SysNativeOssDao, SysNativeOssEntity> implements SysNativeOssService {
    @Value("${htax.profile}")
    private String profile;
    @Value("${htax.pyfile}")
    private String pyfile;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    /*
     * @author ppzz
     * @date 2022/6/18 0018 13:01
     * @Description:
     * 文件上传
     */
    @Override
    public Map<String, String> saveFile(MultipartFile file) {
        Map<String,String> map = new HashMap<>();
        String format = sdf.format(new Date());
        File target_file = FileUtils.upload(file , profile + "/" + format);
        String absolutePath = target_file.getAbsolutePath();
        String link = "/" + format+ "/" + target_file.getName();
        map.put("path" , absolutePath);
        map.put("link" , link);
        map.put("desc" , "如果要通过url方式访问文件，需要配置nginx");
        SysNativeOssEntity entity = new SysNativeOssEntity();
        entity.setUrl(link);
        this.save(entity);
        return map;
    }

    @Override
    public Map<String, String> pathUpload(MultipartFile file) {
        Map<String,String> map = new HashMap<>();
        String format = sdf.format(new Date());
        File target_file = FileUtils.upload(file , pyfile);
        String absolutePath = target_file.getAbsolutePath();
        String link = target_file.getName();
        map.put("path" , absolutePath);
        map.put("link" , link);
        map.put("desc" , "如果要通过url方式访问文件，需要配置nginx");
        SysNativeOssEntity entity = new SysNativeOssEntity();
        entity.setUrl(link);
        this.save(entity);
        return map;
    }
}
