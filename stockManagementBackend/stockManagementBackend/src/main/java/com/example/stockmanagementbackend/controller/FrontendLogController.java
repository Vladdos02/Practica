package com.example.stockmanagementbackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST контролер для збору та логування помилок, що виникають на стороні клієнта (фронтенду).
 * Реалізує вимогу щодо моніторингу JavaScript помилок та дій користувачів.
 */
@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "http://localhost:63342")
public class FrontendLogController {

    // Ініціалізація логера SLF4J для запису у файл сток-менеджменту
    private static final Logger logger = LoggerFactory.getLogger(FrontendLogController.class);

    /**
     * Приймає дані про помилку з фронтенду та записує їх у системний лог.
     * * @param errorDetails Карта з деталями помилки (повідомлення, URL, стек викликів, користувач).
     */
    @PostMapping("/error")
    public void logFrontendError(@RequestBody Map<String, Object> errorDetails) {
        // Витягуємо дані з JSON запиту
        Object user = errorDetails.getOrDefault("user", "anonymous");
        Object message = errorDetails.getOrDefault("message", "No message");
        Object url = errorDetails.getOrDefault("url", "unknown");
        Object stack = errorDetails.getOrDefault("stack", "no stack trace");

        // Формуємо структурований запис у логах рівня ERROR
        // Це дозволить адміністратору бачити помилки браузера у загальному файлі logs/stock-management.log
        logger.error("FRONTEND JS ERROR | Користувач: {} | Повідомлення: {} | URL: {} | Стек: {}",
                user,
                message,
                url,
                stack);
    }

    /**
     * Додатковий ендпоінт для логування важливих дій користувача (Audit Log).
     * * @param actionDetails Деталі дії користувача.
     */
    @PostMapping("/action")
    public void logUserAction(@RequestBody Map<String, Object> actionDetails) {
        Object user = actionDetails.getOrDefault("user", "anonymous");
        Object action = actionDetails.getOrDefault("action", "unknown action");

        // Логуємо дії рівня INFO для аналізу активності користувачів
        logger.info("USER ACTION | Користувач: {} | Дія: {}", user, action);
    }
}