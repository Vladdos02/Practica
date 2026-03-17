package com.example.stockmanagementbackend.dto;

/**
 * Data Transfer Object (DTO) для інкапсуляції даних, що надходять від клієнта
 * при запиті на створення нової резервації.
 * Використовується для безпечного та структурованого передавання параметрів резервації.
 */
public class ReservationRequest {
    private Long productId;
    private Long userId;
    private Integer quantity;

    /**
     * Пустий конструктор, необхідний для десеріалізації JSON (наприклад, Jackson).
     */
    public ReservationRequest() {}

    /**
     * Конструктор для створення об'єкта запиту на резервацію.
     * @param productId Ідентифікатор продукту, який потрібно зарезервувати.
     * @param userId    Ідентифікатор користувача, який робить резервацію.
     * @param quantity  Кількість продукту для резервування.
     */
    public ReservationRequest(Long productId, Long userId, Integer quantity) {
        this.productId = productId;
        this.userId = userId;
        this.quantity = quantity;
    }

    /**
     * Повертає ID продукту, який потрібно зарезервувати.
     * @return ID продукту.
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Встановлює ID продукту.
     * @param productId Новий ID продукту.
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * Повертає ID користувача, який робить резервацію.
     * @return ID користувача.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Встановлює ID користувача.
     * @param userId Новий ID користувача.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Повертає кількість продукту, яку потрібно зарезервувати.
     * @return Кількість продукту.
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Встановлює кількість продукту.
     * @param quantity Нова кількість продукту.
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}