package com.vii.beobssg.config;

import org.h2.server.web.JakartaWebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to enable H2 Database Console in a Spring Boot application.
 *
 * @author Luthfi Aryarizki
 * @date 2025/11/24
 * @description
 * This configuration registers the H2 Web Console servlet to the Spring Boot application,
 * making it accessible via the URL "/h2-console/*". The H2 console is a web interface
 * that allows you to interact with the H2 database directly through a browser.
 *
 * @see <a href="https://www.h2database.com/html/main.html">H2 Database Documentation</a>
 */
@Configuration
public class H2Config {

    @Bean
    public ServletRegistrationBean<JakartaWebServlet> h2ServletRegistration() {
        ServletRegistrationBean<JakartaWebServlet> registrationBean = new ServletRegistrationBean<>(new JakartaWebServlet());
        registrationBean.addUrlMappings("/h2-console/*");
        return registrationBean;
    }
}
