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
    private Long id;
    private String code;
    private String article;
    private String name;
    private String manufacturer;
    private String unitOfMeasurement;
    private BigDecimal vatRate;
    private String manufacturerArticle;
    private String description;
    private Integer quantity;
    private Integer reservedQuantity;
    private BigDecimal price;
    private String locationName;
    private String categoryName;
    private LocalDateTime lastUpdated;

    /**
     * Пустий конструктор, необхідний для роботи десеріалізації JSON (наприклад, Jackson).
     */
    public ProductResponseDTO() {}

    /**
     * Конструктор для перетворення об'єкта {@link Product} моделі на {@link ProductResponseDTO}.
     * Мапує поля моделі на відповідні поля DTO, витягуючи назви категорії та місця.
     * Якщо пов'язані об'єкти (категорія, місце) відсутні, встановлює "N/A".
     *
     * @param product Об'єкт {@link Product} з бази даних.
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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getArticle() { return article; }
    public void setArticle(String article) { this.article = article; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getUnitOfMeasurement() { return unitOfMeasurement; }
    public void setUnitOfMeasurement(String unitOfMeasurement) { this.unitOfMeasurement = unitOfMeasurement; }
    public BigDecimal getVatRate() { return vatRate; }
    public void setVatRate(BigDecimal vatRate) { this.vatRate = vatRate; }
    public String getManufacturerArticle() { return manufacturerArticle; }
    public void setManufacturerArticle(String manufacturerArticle) { this.manufacturerArticle = manufacturerArticle; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}