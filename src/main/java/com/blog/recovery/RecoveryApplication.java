package com.blog.recovery;

import com.blog.recovery.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class RecoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecoveryApplication.class, args);
	}

}
