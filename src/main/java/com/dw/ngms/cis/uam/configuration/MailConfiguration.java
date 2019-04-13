package com.dw.ngms.cis.uam.configuration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("const.bluelink")
@Data
@Getter
@Setter
public class MailConfiguration {

    private String sendBlueMailLinkURL;
    private String sendBlueMailPassword;


    @Profile("dev")
    @Bean
    public String devDatabaseConnection() {
        System.out.println("Connection for UAM_DEV");
        System.out.println(sendBlueMailLinkURL);
        System.out.println(sendBlueMailPassword);
        return "DB connection for DEV Environment";
    }

    @Profile("test")
    @Bean
    public String testDatabaseConnection() {
        System.out.println("Connection to UAM_PRE");
        System.out.println(sendBlueMailLinkURL);
        System.out.println(sendBlueMailPassword);
        return "DB Connection to PRE Environment";
    }

    @Profile("prod")
    @Bean
    public String prodDatabaseConnection() {
        System.out.println("Connection to UAM_PROD");
        System.out.println(sendBlueMailLinkURL);
        System.out.println(sendBlueMailPassword);
        return "DB Connection to PROD Environment";
    }
}
