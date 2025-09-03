package com.lty.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * 权限注解
 * @author lty
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 有任何一个角色({"admin","test","user"})
     *
     * @return
     */
    String[] anyRole() default "";
    
    /**
     * 必须有某个角色
     *
     * @return
     */
    String mustRole() default "";
}
