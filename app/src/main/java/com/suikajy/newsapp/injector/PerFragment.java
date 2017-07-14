package com.suikajy.newsapp.injector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by suikajy on 2017/4/6.
 * dagger单例标识, 标识每个fragment中实现单例
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerFragment {
}
