package com.example.recipea.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mahdi Sharifi
 * Change Swagger Header UI
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("Movie API")
                        .version("v1")
                        .description("Give your username '/v1/users/login' and get the Bearer Token," +
                                " Then click on Authorize button at the up right of the page." +
                                " Paste the token here. At last click on 'Authorize'. Enjoy it ;-)")
                        .contact(new Contact()
                                .name("Mahdi Sharifi")
                                .url("https://www.linkedin.com/in/mahdisharifi/")
                                .email("mahdi.elu@gmail.com")
                        )
                );
    }
}
