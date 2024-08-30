package com.banca.fibonacci;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FibonacciApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(FibonacciApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FibonacciApplication.class, args);
		LOGGER.info("Application running");
	}


}
