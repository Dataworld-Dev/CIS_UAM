package com.dw.ngms.cis.uam.config;

import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Component
@Configuration
public class DatetimeAndTimezoneConfig {

	private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    
	@PostConstruct
	public void init() {		
		System.out.println(java.util.TimeZone.getDefault());
//		TimeZone.setDefault(TimeZone.getTimeZone("UTC+2"));//"Africa/Johannesburg"
//	    System.out.println("Date in UTC: " + new Date().toString());        
	}//init  	
 
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(dateTimeFormat);
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        };
    }
}
