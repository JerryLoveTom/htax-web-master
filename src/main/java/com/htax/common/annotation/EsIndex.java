/**
 * Copyright (c) 2016-2019 HTAX All rights reserved.
 *
 * 北京华泰安信科技有限公司
 *
 * 版权所有，侵权必究！
 */

package com.htax.common.annotation;

import java.lang.annotation.*;

/**
 * @description: 索引数据至es
 * @author:wangJ
 * @date: 2022/5/17 11:04
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EsIndex {

    String index() default "";
    String indexType() default "";

}
