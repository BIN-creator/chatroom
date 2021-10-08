package com.itheima.mm.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author ：seanyang
 * @date ：Created in 2020/1/3
 * @description ：
 * @version: 1.0
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	/**
	 * -设置url后缀模式匹配规则
	 * -该设置匹配所有的后缀，使用.do或.action都可以
	 */
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(true)	//设置是否是后缀模式匹配,即:/test.*
			.setUseTrailingSlashMatch(true);	//设置是否自动后缀路径模式匹配,即：/test/
	}

}

