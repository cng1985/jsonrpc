package com.quhaodian.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * @author 年高
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestFul {

	/**
	 * 客服端访问的接口名称
	 * @return 接口名称
	 */
	String value() default "";
	/**
	 * 实现的接口
	 * @return
	 */
    Class api() default void.class;

}