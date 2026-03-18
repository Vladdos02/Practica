package com.example.stockmanagementbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфігурація CORS.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /** Конструктор за замовчуванням. */
    public CorsConfig() {}

    /**
     * Додає мапінги CORS для всіх ендпоінтів.
     * Дозволяє запити з "http://localhost:63342" з певними HTTP-методами та заголовками, включаючи облікові дані.
     * @param registry Об'єкт CorsRegistry для реєстрації мапінгів.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:63342")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}