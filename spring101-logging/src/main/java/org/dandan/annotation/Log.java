package org.dandan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @Target(ElementType.METHOD): 這表示 @Log 注解僅能夠標記在方法上，而不是類別或其他元素。
 *
 * @Retention(RetentionPolicy.RUNTIME): 這表示該注解在運行時仍然可用，允許透過反射機制獲取和使用該注解的資訊。
 *
 * public @interface Log: 這定義了一個自訂的注解，名為 Log。
 *
 * String value() default "";: 這是一個注解屬性，允許在使用 @Log 注解時提供一個可選的值。這裡的 value 屬性預設為空字串，你可以根據實際需求指定不同的預設值。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String value() default "";
}
