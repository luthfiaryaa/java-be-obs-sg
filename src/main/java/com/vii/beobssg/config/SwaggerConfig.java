package com.vii.beobssg.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${application-description}") String appDescription,
                                 @Value("${application-version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("JAVA_TEST_BE_OBS_SG")
                        .description(appDescription)
                        .version(appVersion)
                        .contact(new Contact()
                                .name("luthfi")
                                .email("luthfiaryarizki25@gmail.com")
                                .url("http://localhost:8080"))
                        .termsOfService("TOC")
                        .license(new License().name("License").url("http://localhost:8080")));
    }
}
