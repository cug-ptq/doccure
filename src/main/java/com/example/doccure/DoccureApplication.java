package com.example.doccure;

import com.example.doccure.utils.ApplicationContextFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.example.doccure.dao")
@EnableScheduling
public class DoccureApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DoccureApplication.class, args);
        ApplicationContextFactory.setApplicationContext(applicationContext);
    }
}
