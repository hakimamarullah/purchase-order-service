package com.starline.purchase.order.config;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/21/2024 7:39 PM
@Last Modified 6/21/2024 7:39 PM
Version 1.0
*/

import com.starline.purchase.order.config.constant.AppConstant;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Value("${spring.application.name}")
    private String appName;

    @Bean(AppConstant.NO_AUTH_PATHS)
    public String[] noAuthPaths() {
        return new String[]{
                "/error**", "/swagger-ui/**",
                "/api/v1/auth/login", "/rest-api-docs/**", "/rest-api-docs.json",
                "/api/v1/auth/register", "/", "/api/v1/products/**", "/actuator/**"
        };
    }


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(appName)
                        .version("1.0.0")
                        .description("Purchase Order Service")
                        .contact(new Contact()
                                .name("Hakim Amarullah")
                                .email("hakimamarullah@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }

}
