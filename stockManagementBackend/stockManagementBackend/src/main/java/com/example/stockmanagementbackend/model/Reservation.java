package com.example.stockmanagementbackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Модель сутності, що представляє резервацію товару.
 * Відстежує, який товар, в якій кількості, ким і коли був зарезервований, а також його поточний статус.
 */
@Entity
@Table(name = "reservations")
public class Reservation {

    /**
     * Перелік можливих статусів резервації.
     * PENDING - резервація очікує підтвердження/виконання.
     * FULFILLED - резервація виконана (товар відвантажено, кількість на складі зменшилася).
     * CANCELED - резервація скасована (товар повернуто на склад).
     */
    public enum ReservationStatus {
        PENDING, FULFILLED, CANCELED
    }

    /**
     * Унікальний ідентифікатор резервації. Автоматично генерується базою даних.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Товар, який був зарезервований.
     * Зв'язок "багато до одного" з сутністю {@link Product}.
     * Завантажується ліниво (LAZY), стовпець не може бути null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Користувач, який зробив резервацію.
     * Зв'язок "багато до одного" з сутністю {@link User}.
     * Завантажується ліниво (LAZY), стовпець не може бути null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Кількість зарезервованого товару. Не може бути null.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Дата та час створення резервації.
     * Встановлюється автоматично при створенні об'єкта та не може бути оновлена.
     */
    @Column(name = "reservation_date", updatable = false)
    private LocalDateTime reservationDate;

    /**
     * Поточний статус резервації.
     * Зберігається як рядок (VARCHAR) у базі даних.
     * Не може бути null.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    /**
     * Пустий конструктор за замовчуванням, необхідний для JPA.
     * Встановлює {@code reservationDate} на поточний час
     * та {@code status} на {@link ReservationStatus#PENDING}.
     */
    public Reservation() {
        this.reservationDate = LocalDateTime.now();
        this.status = ReservationStatus.PENDING;
    }

    /**
     * Конструктор для створення нового об'єкта резервації.
     * Встановлює {@code reservationDate} на поточний час
     * та {@code status} на {@link ReservationStatus#PENDING}.
     *
     * @param product Об'єкт товару, що резервується.
     * @param user    Користувач, який створює резервацію.
     * @param quantity Кількість товару для резервування.
     */
    public Reservation(Product product, User user, Integer quantity) {
        this.product = product;
        this.user = user;
        this.quantity = quantity;
        this.reservationDate = LocalDateTime.now();
        this.status = ReservationStatus.PENDING;
    }

    /**
     * Повертає ідентифікатор резервації.
     * @return Ідентифікатор резервації.
     */
    public Long getId() {
        return id;
    }

    /**
     * Встановлює ідентифікатор резервації.
     * @param id Новий ідентифікатор резервації.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Повертає об'єкт товару, який був зарезервований.
     * @return Об'єкт {@link Product}.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Встановлює об'єкт товару для резервації.
     * @param product Новий об'єкт {@link Product}.
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Повертає об'єкт користувача, який зробив резервацію.
     * @return Об'єкт {@link User}.
     */
    public User getUser() {
        return user;
    }

    /**
     * Встановлює об'єкт користувача для резервації.
     * @param user Новий об'єкт {@link User}.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Повертає кількість зарезервованого товару.
     * @return Кількість товару.
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Встановлює кількість зарезервованого товару.
     * @param quantity Нова кількість товару.
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Повертає дату та час створення резервації.
     * @return Дата та час резервації.
     */
    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    /**
     * Встановлює дату та час створення резервації.
     * @param reservationDate Нова дата та час резервації.
     */
    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    /**
     * Повертає поточний статус резервації.
     * @return Статус резервації як елемент {@link ReservationStatus} Enum.
     */
    public ReservationStatus getStatus() {
        return status;
    }

    /**
     * Встановлює поточний статус резервації.
     * @param status Новий статус резервації як елемент {@link ReservationStatus} Enum.
     */
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    /**
     * Додатковий метод для встановлення статусу резервації з рядка.
     * Перетворює вхідний рядок у відповідний елемент {@link ReservationStatus} Enum.
     * @param statusString Рядок, що представляє статус (наприклад, "PENDING", "FULFILLED").
     * @throws IllegalArgumentException якщо вхідний рядок не відповідає жодному з допустимих статусів.
     */
    public void setStatus(String statusString) {
        try {
            this.status = ReservationStatus.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid status string for Reservation: " + statusString);
            throw new IllegalArgumentException("Invalid reservation status: " + statusString);
        }
    }
}