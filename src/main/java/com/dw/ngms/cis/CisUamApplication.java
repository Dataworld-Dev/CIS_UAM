package com.dw.ngms.cis;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.dw.ngms.cis.uam.storage.StorageService;

@ServletComponentScan
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class CisUamApplication extends SpringBootServletInitializer {

    @Resource
    StorageService storageService;


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CisUamApplication .class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CisUamApplication.class, args);
    }

    @PostConstruct
    public void init(){
//      TimeZone.setDefault(TimeZone.getTimeZone("UTC+2"));
    	//TimeZone.setDefault(TimeZone.getTimeZone("GMT+2"));
    }
    
}
