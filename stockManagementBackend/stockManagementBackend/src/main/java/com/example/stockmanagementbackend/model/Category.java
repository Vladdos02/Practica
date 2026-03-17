package com.example.stockmanagementbackend.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Модель сутності, що представляє категорію товару у системі.
 * Кожна категорія має унікальне ім'я та ідентифікатор.
 */
@Entity
@Table(name = "categories")
@Data
public class Category {

    /**
     * Унікальний ідентифікатор категорії. Автоматично генерується базою даних.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Назва категорії (наприклад, "Фільтри", "Масла").
     * Не може бути null та має бути унікальною.
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Пустий конструктор, необхідний для JPA (Hibernate).
     */
    public Category() {
    }

    /**
     * Конструктор для створення нового об'єкта Category з вказаним ім'ям.
     * @param name Назва категорії.
     */
    public Category(String name) {
        this.name = name;
    }
}