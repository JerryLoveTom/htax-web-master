package com.htax.modules.oss.controller;

import com.htax.common.exception.HTAXException;
import com.htax.common.utils.R;
import com.htax.modules.oss.service.SysNativeOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;

/** 本地文件上传
 * @author ppzz
 * @date 2022-06-17 14:29
 */
@RestController
@RequestMapping("sys/nativeoss")
@Api(tags = "本地文件上传")
public class NativeOssController {
    @Autowired
    private SysNativeOssService ossService;

    @PostMapping("/upload")
    @ApiOperation("上传文件(如果需要访问文件，则需要将文件的路径配置到nginx中)")
    public R upload(@RequestParam("file") MultipartFile file , HttpServletRequest request) throws Exception {
        if (file.isEmpty()) {
            throw new HTAXException("上传文件不能为空");
        }
        Map<String,String> map = ossService.saveFile(file);
        return R.ok().put("item", map);
    }
    @PostMapping("/path/upload")
    @ApiOperation("上传py文件，不要创建以日期分割的文件夹)")
    public R pathUpload(@RequestParam("file") MultipartFile file , HttpServletRequest request) throws Exception {
        if (file.isEmpty()) {
            throw new HTAXException("上传文件不能为空");
        }
        Map<String,String> map = ossService.pathUpload(file);
        return R.ok().put("item", map);
    }

    @PostMapping("/refile")
    @ApiOperation("删除指定路径下的文件")
    public R reFile(@RequestParam("path") String path){
        boolean result = FileSystemUtils.deleteRecursively(new File(path));
        return result ? R.ok() : R.error("文件被占用或不存在！");
    }
}
