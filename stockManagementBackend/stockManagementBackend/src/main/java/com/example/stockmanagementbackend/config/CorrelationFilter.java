package com.example.stockmanagementbackend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationFilter implements Filter {
    private static final String TRACE_HEADER = "X-Correlation-ID";
    private static final String MDC_KEY = "traceId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Зчитуємо ID з фронтенду або генеруємо новий (якщо запит не з браузера)
        String traceId = httpRequest.getHeader(TRACE_HEADER);
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString();
        }

        // Кладемо ID в контекст логування (MDC)
        MDC.put(MDC_KEY, traceId);

        try {
            chain.doFilter(request, response);
        } finally {
            // Очищуємо після завершення запиту
            MDC.remove(MDC_KEY);
        }
    }
}