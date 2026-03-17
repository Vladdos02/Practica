package com.example.stockmanagementbackend.dto;

import com.example.stockmanagementbackend.model.Reservation;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) для представлення інформації про резервацію у відповідях API.
 * Включає деталі про продукт, користувача та саму резервацію, оптимізовані для клієнтського відображення.
 */
public class ReservationResponseDTO {
    private Long id;
    private ProductResponseDTO product;
    private Long userId;
    private String username;
    private Integer quantity;
    private LocalDateTime reservationDate;
    private String status;

    /**
     * Пустий конструктор, необхідний для роботи десеріалізації JSON.
     */
    public ReservationResponseDTO() {}

    /**
     * Конструктор для перетворення об'єкта {@link Reservation} моделі на {@link ReservationResponseDTO}.
     * Мапує поля моделі на відповідні поля DTO, ініціалізує вкладений {@link ProductResponseDTO}
     * та витягує дані користувача.
     *
     * @param reservation Об'єкт {@link Reservation} з бази даних.
     */
    public ReservationResponseDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.quantity = reservation.getQuantity();
        this.reservationDate = reservation.getReservationDate();
        this.status = reservation.getStatus().name();

        if (reservation.getProduct() != null) {
            this.product = new ProductResponseDTO(reservation.getProduct());
        }

        if (reservation.getUser() != null) {
            this.userId = reservation.getUser().getId();
            this.username = reservation.getUser().getUsername();
        }
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
     * Повертає вкладений об'єкт ProductResponseDTO, що представляє деталі зарезервованого продукту.
     * @return ProductResponseDTO зарезервованого продукту.
     */
    public ProductResponseDTO getProduct() {
        return product;
    }

    /**
     * Встановлює вкладений об'єкт ProductResponseDTO.
     * @param product Новий ProductResponseDTO.
     */
    public void setProduct(ProductResponseDTO product) {
        this.product = product;
    }

    /**
     * Повертає ідентифікатор користувача, який зробив резервацію.
     * @return Ідентифікатор користувача.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Встановлює ідентифікатор користувача.
     * @param userId Новий ідентифікатор користувача.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Повертає ім'я користувача, який зробив резервацію.
     * @return Ім'я користувача.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Встановлює ім'я користувача.
     * @param username Нове ім'я користувача.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Повертає кількість зарезервованого продукту.
     * @return Кількість продукту.
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Встановлює кількість зарезервованого продукту.
     * @param quantity Нова кількість продукту.
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
     * Повертає поточний статус резервації (наприклад, "PENDING", "FULFILLED", "CANCELED").
     * @return Статус резервації.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Встановлює поточний статус резервації.
     * @param status Новий статус резервації.
     */
    public void setStatus(String status) {
        this.status = status;
    }
}