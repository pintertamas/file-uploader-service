package com.tamaspinter.fileuploadservice;

import com.tamaspinter.fileuploadservice.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class FileUploadServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileUploadServiceApplication.class, args);
    }

}
