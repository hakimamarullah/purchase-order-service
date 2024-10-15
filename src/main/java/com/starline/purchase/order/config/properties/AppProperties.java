package com.starline.purchase.order.config.properties;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/23/2024 7:48 PM
@Last Modified 6/23/2024 7:48 PM
Version 1.0
*/

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app")
@Setter
@Getter
public class AppProperties {

    private long JWT_TOKEN_AGE = 3600;

    private String JWT_ISSUER = "purchase-order-service";

}
