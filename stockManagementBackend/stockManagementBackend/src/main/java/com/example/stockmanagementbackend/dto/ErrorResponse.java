package com.example.stockmanagementbackend.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.MDC;

/**
 * Data Transfer Object (DTO) для стандартизованого представлення інформації про помилки.
 * Використовується для надання детальних повідомлень про помилки у відповідях API.
 */
public class ErrorResponse {
    private String traceId;
    private String errorId;
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    /**
     * Конструктор для створення об'єкта ErrorResponse.
     * Автоматично встановлює поточний час як timestamp.
     *
     * @param status  HTTP статус помилки (наприклад, 400, 404, 500).
     * @param error   Коротка назва HTTP-статусу (наприклад, "Bad Request", "Not Found").
     * @param message Детальне повідомлення про помилку.
     * @param path    Шлях запиту, який спричинив помилку.
     */
    public ErrorResponse(int status, String error, String message, String path) {
        this.traceId = MDC.get("traceId");
        this.errorId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    /**
     * Повертає ID помилки.
     * @return ID помилки.
     */
    public String getTraceId() { return traceId; }

    /**
     * Повертає ID помилки.
     * @return ID помилки.
     */
    public String getErrorId() { return errorId; }

    /**
     * Повертає час виникнення помилки.
     * @return Мітка часу помилки.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Встановлює час виникнення помилки.
     * @param timestamp Нова мітка часу.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Повертає HTTP статус код помилки.
     * @return HTTP статус код.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Встановлює HTTP статус код помилки.
     * @param status Новий HTTP статус код.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Повертає загальну назву помилки.
     * @return Загальна назва помилки.
     */
    public String getError() {
        return error;
    }

    /**
     * Встановлює загальну назву помилки.
     * @param error Нова назва помилки.
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Повертає детальне повідомлення про помилку.
     * @return Детальне повідомлення про помилку.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Встановлює детальне повідомлення про помилку.
     * @param message Нове повідомлення про помилку.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Повертає шлях запиту, який спричинив помилку.
     * @return Шлях запиту.
     */
    public String getPath() {
        return path;
    }

    /**
     * Встановлює шлях запиту, який спричинив помилку.
     * @param path Новий шлях запиту.
     */
    public void setPath(String path) {
        this.path = path;
    }
}