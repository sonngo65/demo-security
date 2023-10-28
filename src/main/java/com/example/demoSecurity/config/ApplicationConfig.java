package com.example.demoSecurity.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ApplicationConfig {

	@Bean
	public Path getFileLocation() {
		return Paths.get("src/main/java/com/example/demoSecurity/upload");
	}
	@Bean 
	public ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}
}
