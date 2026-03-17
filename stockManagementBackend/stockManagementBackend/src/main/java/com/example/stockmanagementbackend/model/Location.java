package com.example.stockmanagementbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Модель сутності, що представляє місце зберігання товару на складі.
 * Кожне місце має унікальне ім'я та може мати додатковий опис.
 */
@Entity
@Table(name = "locations")
@Data
public class Location {

    /**
     * Унікальний ідентифікатор місця зберігання. Автоматично генерується базою даних.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Назва місця зберігання (наприклад, "Склад 1, Секція А").
     * Не може бути null та має бути унікальною.
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Додатковий опис місця зберігання. Може бути null.
     */
    @Column(name = "description")
    private String description;

    /**
     * Пустий конструктор, необхідний для JPA (Hibernate).
     */
    public Location() {
    }

    /**
     * Конструктор для створення нового об'єкта Location з вказаними ім'ям та описом.
     * @param name Назва місця зберігання.
     * @param description Додатковий опис місця зберігання.
     */
    public Location(String name, String description) {
        this.name = name;
        this.description = description;
    }
}