package com.wcj.common.annotation;

import java.lang.annotation.*;

/**
 * Created by chengjie.wang on 2017/12/29.
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PropertiesMapping {

    String value();

}
