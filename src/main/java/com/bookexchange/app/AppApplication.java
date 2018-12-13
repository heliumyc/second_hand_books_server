package com.bookexchange.app;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

// 整个程序的健壮性较差, 主要是没有使用spring的事务管理讲mysql的事务和lucene的事务结合起来
// lucene中的操作commit不能包含在函数中, 应该单独列出来, 同时暴露rollback方法
// mysql同理, 然后在spring的transaction中统一管理
// 后来经过分析, lucene的事务管理实现会严重拖慢系统速度, commit并不能频繁使用
// 那么就可以放弃搜索索引的一致性, 只维护mysql中的数据一致性, 而每天或者隔一段时间重新建立索引, 刷新脏数据
@SpringBootApplication
@MapperScan("com.bookexchange.app.model.dao")
public class AppApplication {
    @Bean
    public ServletRegistrationBean jerseyServlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/api/*");
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
        return registration;
    }

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }
}
