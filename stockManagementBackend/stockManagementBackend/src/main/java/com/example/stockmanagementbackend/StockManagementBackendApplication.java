package com.example.stockmanagementbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Головний клас додатка.
 */
@SpringBootApplication
public class StockManagementBackendApplication {

    /** Конструктор за замовчуванням. */
    public StockManagementBackendApplication() {}

    /**
     * Головний метод, який запускає Spring Boot додаток.
     *
     * @param args Аргументи командного рядка, передані при запуску додатка.
     */
    public static void main(String[] args) {
        SpringApplication.run(StockManagementBackendApplication.class, args);
    }

}