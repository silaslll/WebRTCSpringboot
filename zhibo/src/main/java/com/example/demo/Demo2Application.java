package com.example.demo;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@SpringBootApplication
public class Demo2Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Demo2Application.class, args);
	}
	
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
       return new ServerEndpointExporter();
    }
    
    @Bean public MultipartConfigElement multipartConfigElement() {
    	MultipartConfigFactory factory = new MultipartConfigFactory(); //  单个数据大小 
    	factory.setMaxFileSize("10240KB"); // KB,MB /// 总上传数据大小 
    	factory.setMaxRequestSize("102400KB"); return factory.createMultipartConfig();
    	}
}
