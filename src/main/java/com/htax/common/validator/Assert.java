/**
 * Copyright (c) 2016-2019 HTAX All rights reserved.
 *
 * 北京华泰安信科技有限公司
 *
 * 版权所有，侵权必究！
 */

package com.htax.common.validator;

import com.htax.common.exception.HTAXException;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 *
 * @author Mark ppzz
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new HTAXException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new HTAXException(message);
        }
    }
}
