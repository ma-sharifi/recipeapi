package com.example.recipea;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(name = "JWTtoken", scheme = "bearer", bearerFormat = "JWT", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@Slf4j
public class RecipeaApplication {
	public static void main(String[] args) {
		if(log.isDebugEnabled()) log.warn("##IMPORTANT NOTE: Debug mode is activated! You/Customer can see the details of your errors!##");
		SpringApplication.run(RecipeaApplication.class, args);
	}

}
