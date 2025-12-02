package com.example.stockmanagementbackend.model;

import jakarta.persistence.*;

/**
 * Модель сутності, що представляє користувача у системі управління запасами.
 * Містить інформацію про ім'я користувача, пароль та роль у системі.
 */
@Entity
@Table(name = "users")
public class User {
    /**
     * Унікальний ідентифікатор користувача. Автоматично генерується базою даних.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Унікальне ім'я користувача (логін). Не може бути null та має бути унікальним.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Пароль користувача. Не може бути null.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Роль користувача у системі (наприклад, "ADMIN", "EMPLOYEE", "WAREHOUSE_MANAGER").
     * Не може бути null.
     */
    @Column(nullable = false)
    private String role; // ADMIN, EMPLOYEE, WAREHOUSE_MANAGER

    /**
     * Пустий конструктор за замовчуванням, необхідний для JPA.
     */
    public User() {
    }

    /**
     * Конструктор для створення нового об'єкта користувача.
     * @param username Унікальне ім'я користувача.
     * @param password Пароль користувача.
     * @param role Роль користувача у системі.
     */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Повертає ідентифікатор користувача.
     * @return Ідентифікатор користувача.
     */
    public Long getId() {
        return id;
    }

    /**
     * Встановлює ідентифікатор користувача.
     * @param id Новий ідентифікатор користувача.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Повертає ім'я користувача (логін).
     * @return Ім'я користувача.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Встановлює ім'я користувача (логін).
     * @param username Нове ім'я користувача.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Повертає пароль користувача.
     * @return Пароль користувача.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Встановлює пароль користувача.
     * @param password Новий пароль користувача.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Повертає роль користувача у системі.
     * @return Роль користувача.
     */
    public String getRole() {
        return role;
    }

    /**
     * Встановлює роль користувача у системі.
     * @param role Нова роль користувача.
     */
    public void setRole(String role) {
        this.role = role;
    }
}