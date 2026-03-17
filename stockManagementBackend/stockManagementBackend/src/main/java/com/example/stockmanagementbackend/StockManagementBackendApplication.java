package com.example.stockmanagementbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Головний клас Spring Boot додатка для системи управління запасами.
 * Цей клас є точкою входу для запуску всього бекенд-сервісу.
 */
@SpringBootApplication
public class StockManagementBackendApplication {

    /**
     * Головний метод, який запускає Spring Boot додаток.
     *
     * @param args Аргументи командного рядка, передані при запуску додатка.
     */
    public static void main(String[] args) {
        SpringApplication.run(StockManagementBackendApplication.class, args);
    }

}