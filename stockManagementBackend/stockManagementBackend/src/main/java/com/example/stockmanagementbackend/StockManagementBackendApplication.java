package com.example.stockmanagementbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Головний клас додатка.
 */
@SpringBootApplication
public class StockManagementBackendApplication {

    /** Конструктор за замовчуванням. */
    public StockManagementBackendApplication() {}
    private static final Logger logger = LoggerFactory.getLogger(StockManagementBackendApplication.class);
    /**
     * Головний метод, який запускає Spring Boot додаток.
     *
     * @param args Аргументи командного рядка, передані при запуску додатка.
     */
    public static void main(String[] args) {
        SpringApplication.run(StockManagementBackendApplication.class, args);
    }

}