package com.example.stockmanagementbackend.dto;

import com.example.stockmanagementbackend.model.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) для представлення інформації про продукт у відповідях API.
 * Надає спрощену та плоску структуру даних продукту, оптимізовану для відображення на клієнті,
 * включаючи назви категорії та місця замість повних об'єктів.
 */
public class ProductResponseDTO {
    /** Унікальний ідентифікатор продукту. */
    private Long id;
    /** Унікальний код продукту. */
    private String code;
    /** Артикул продукту. */
    private String article;
    /** Назва продукту. */
    private String name;
    /** Виробник продукту. */
    private String manufacturer;
    /** Одиниця виміру товару. */
    private String unitOfMeasurement;
    /** Ставка ПДВ для товару. */
    private BigDecimal vatRate;
    /** Артикул від виробника. */
    private String manufacturerArticle;
    /** Детальний опис продукту. */
    private String description;
    /** Загальна кількість на складі. */
    private Integer quantity;
    /** Кількість зарезервованого товару. */
    private Integer reservedQuantity;
    /** Ціна за одиницю товару. */
    private BigDecimal price;
    /** Назва місця зберігання на складі. */
    private String locationName;
    /** Назва категорії, до якої належить товар. */
    private String categoryName;
    /** Дата та час останнього оновлення запису. */
    private LocalDateTime lastUpdated;

    /**
     * Пустий конструктор, необхідний для роботи десеріалізації JSON (наприклад, Jackson).
     */
    public ProductResponseDTO() {}

    /**
     * Конструктор для перетворення об'єкта моделі на DTO.
     * Мапує поля моделі на відповідні поля DTO, витягуючи назви категорії та місця.
     *
     * @param product Об'єкт моделі Product.
     */
    public ProductResponseDTO(Product product) {
        this.id = product.getId();
        this.code = product.getCode();
        this.article = product.getArticle();
        this.name = product.getName();
        this.manufacturer = product.getManufacturer();
        this.unitOfMeasurement = product.getUnitOfMeasurement();
        this.vatRate = product.getVatRate();
        this.manufacturerArticle = product.getManufacturerArticle();
        this.description = product.getDescription();
        this.quantity = product.getQuantity();
        this.reservedQuantity = product.getReservedQuantity() != null ? product.getReservedQuantity() : 0;
        this.price = product.getPrice();
        this.lastUpdated = product.getLastUpdated();

        if (product.getLocation() != null) {
            this.locationName = product.getLocation().getName();
        } else {
            this.locationName = "N/A";
        }
        if (product.getCategory() != null) {
            this.categoryName = product.getCategory().getName();
        } else {
            this.categoryName = "N/A";
        }
    }

    /**
     * Повертає унікальний ідентифікатор продукту.
     * @return Ідентифікатор типу Long.
     */
    public Long getId() { return id; }

    /**
     * Встановлює унікальний ідентифікатор продукту.
     * @param id Новий ідентифікатор.
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Повертає внутрішній код продукту.
     * @return Код продукту.
     */
    public String getCode() { return code; }

    /**
     * Встановлює внутрішній код продукту.
     * @param code Новий код продукту.
     */
    public void setCode(String code) { this.code = code; }

    /**
     * Повертає артикул продукту.
     * @return Артикул типу String.
     */
    public String getArticle() { return article; }

    /**
     * Встановлює артикул продукту.
     * @param article Новий артикул.
     */
    public void setArticle(String article) { this.article = article; }

    /**
     * Повертає назву продукту.
     * @return Назва продукту.
     */
    public String getName() { return name; }

    /**
     * Встановлює назву продукту.
     * @param name Нова назва.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Повертає назву виробника продукту.
     * @return Виробник продукту.
     */
    public String getManufacturer() { return manufacturer; }

    /**
     * Встановлює назву виробника продукту.
     * @param manufacturer Новий виробник.
     */
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    /**
     * Повертає одиницю виміру продукту.
     * @return Одиниця виміру (наприклад, шт).
     */
    public String getUnitOfMeasurement() { return unitOfMeasurement; }

    /**
     * Встановлює одиницю виміру продукту.
     * @param unitOfMeasurement Нова одиниця виміру.
     */
    public void setUnitOfMeasurement(String unitOfMeasurement) { this.unitOfMeasurement = unitOfMeasurement; }

    /**
     * Повертає ставку ПДВ для продукту.
     * @return Ставка ПДВ у форматі BigDecimal.
     */
    public BigDecimal getVatRate() { return vatRate; }

    /**
     * Встановлює ставку ПДВ для продукту.
     * @param vatRate Нова ставка ПДВ.
     */
    public void setVatRate(BigDecimal vatRate) { this.vatRate = vatRate; }

    /**
     * Повертає артикул виробника продукту.
     * @return Артикул від виробника.
     */
    public String getManufacturerArticle() { return manufacturerArticle; }

    /**
     * Встановлює артикул виробника продукту.
     * @param manufacturerArticle Новий артикул виробника.
     */
    public void setManufacturerArticle(String manufacturerArticle) { this.manufacturerArticle = manufacturerArticle; }

    /**
     * Повертає текстовий опис продукту.
     * @return Опис продукту.
     */
    public String getDescription() { return description; }

    /**
     * Встановлює текстовий опис продукту.
     * @param description Новий опис.
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Повертає загальну кількість продукту на складі.
     * @return Кількість на складі.
     */
    public Integer getQuantity() { return quantity; }

    /**
     * Встановлює загальну кількість продукту на складі.
     * @param quantity Нова кількість.
     */
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    /**
     * Повертає кількість зарезервованого продукту.
     * @return Зарезервована кількість.
     */
    public Integer getReservedQuantity() { return reservedQuantity; }

    /**
     * Встановлює кількість зарезервованого продукту.
     * @param reservedQuantity Нова зарезервована кількість.
     */
    public void setReservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    /**
     * Повертає ціну продукту.
     * @return Ціна у форматі BigDecimal.
     */
    public BigDecimal getPrice() { return price; }

    /**
     * Встановлює ціну продукту.
     * @param price Нова ціна.
     */
    public void setPrice(BigDecimal price) { this.price = price; }

    /**
     * Повертає назву місця зберігання продукту.
     * @return Назва локації на складі.
     */
    public String getLocationName() { return locationName; }

    /**
     * Встановлює назву місця зберігання продукту.
     * @param locationName Нова назва локації.
     */
    public void setLocationName(String locationName) { this.locationName = locationName; }

    /**
     * Повертає назву категорії продукту.
     * @return Назва категорії.
     */
    public String getCategoryName() { return categoryName; }

    /**
     * Встановлює назву категорії продукту.
     * @param categoryName Нова назва категорії.
     */
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    /**
     * Повертає дату та час останнього оновлення продукту.
     * @return Об'єкт LocalDateTime останнього оновлення.
     */
    public LocalDateTime getLastUpdated() { return lastUpdated; }

    /**
     * Встановлює дату та час останнього оновлення продукту.
     * @param lastUpdated Нова дата оновлення.
     */
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}