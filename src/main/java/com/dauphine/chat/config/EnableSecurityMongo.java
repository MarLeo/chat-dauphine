package com.dauphine.chat.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author belgacea
 * @date 26/12/2016
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ MongoDBConfig.class })
public @interface EnableSecurityMongo {
}
