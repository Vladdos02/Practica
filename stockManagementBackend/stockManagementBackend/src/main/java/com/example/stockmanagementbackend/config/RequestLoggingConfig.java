package com.example.stockmanagementbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Конфігурація для логування вхідних HTTP-запитів.
 */
@Configuration
public class RequestLoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();

        // Включаємо логування параметрів запиту (після знаку ?)
        filter.setIncludeQueryString(true);

        // Включаємо логування тіла запиту (JSON, який надсилає фронтенд)
        filter.setIncludePayload(true);

        // Максимальна довжина тіла запиту, яка буде записана в лог
        filter.setMaxPayloadLength(10000);

        // Вимикаємо логування заголовків (Headers) для безпеки
        filter.setIncludeHeaders(false);

        // Префікс, який буде додано перед повідомленням у логах
        filter.setAfterMessagePrefix("REQUEST DATA: ");

        return filter;
    }
}