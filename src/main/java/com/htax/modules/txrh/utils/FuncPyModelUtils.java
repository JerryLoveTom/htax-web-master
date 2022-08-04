package com.htax.modules.txrh.utils;

import com.htax.common.utils.HttpUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: htax-web-master
 * @BelongsPackage: com.htax.modules.txrh.util
 * @Author: ppzz
 * @CreateTime: 2022-07-14  18:08
 * @Description: TODO
 * @Version: 1.0
 */
public class FuncPyModelUtils {

    public JSONObject func(String url, Map<String, Object>map) throws IOException {
        JSONObject jsonObject = HttpUtils.postMap(url, map);
        return jsonObject;
    }
}
