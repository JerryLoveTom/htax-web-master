/**
 * Copyright (c) 2018 HTAX All rights reserved.
 *
 * 北京华泰安信科技有限公司
 *
 * 版权所有，侵权必究！
 */

package com.htax.datasource.annotation;

import java.lang.annotation.*;

/**
 * 多数据源注解
 *
 * @author Mark ppzz
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    String value() default "";
}
