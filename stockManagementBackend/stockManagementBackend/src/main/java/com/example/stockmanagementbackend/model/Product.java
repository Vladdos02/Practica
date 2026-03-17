package com.example.stockmanagementbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Модель сутності, що представляє товар у системі управління запасами.
 * Містить детальну інформацію про товар, його кількість, ціну, місце зберігання та категорію.
 */
@Entity
@Table(name = "products")
public class Product {
    /**
     * Унікальний ідентифікатор товару. Автоматично генерується базою даних.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Унікальний код товару. Не може бути null та має бути унікальним.
     */
    @Column(nullable = false, unique = true)
    private String code;

    /**
     * Артикул товару. Не може бути null.
     */
    @Column(nullable = false)
    private String article;

    /**
     * Назва товару. Не може бути null.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Виробник товару.
     */
    private String manufacturer;

    /**
     * Одиниця виміру товару (наприклад, "шт.", "кг", "л").
     */
    @Column(name = "unit_of_measurement")
    private String unitOfMeasurement;

    /**
     * Артикул виробника товару.
     */
    @Column(name = "manufacturer_article")
    private String manufacturerArticle;

    /**
     * Додатковий опис товару.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Поточна загальна кількість товару на складі. Не може бути null.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Кількість товару, яка була зарезервована.
     * За замовчуванням дорівнює 0. Не може бути null.
     */
    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity = 0;

    /**
     * Ціна товару. Не може бути null.
     */
    @Column(nullable = false)
    private BigDecimal price;

    /**
     * Дата та час останнього оновлення інформації про товар.
     * Автоматично встановлюється при створенні/оновленні.
     */
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    /**
     * Ставка ПДВ для товару.
     */
    @Column(name = "vat_rate")
    private BigDecimal vatRate;

    /**
     * Категорія, до якої належить товар.
     * Зв'язок "багато до одного" з сутністю {@link Category}.
     * Завантажується ліниво (LAZY), стовпець не може бути null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    /**
     * Місце зберігання товару на складі.
     * Зв'язок "багато до одного" з сутністю {@link Location}.
     * Завантажується ліниво (LAZY), стовпець не може бути null.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    /**
     * Пустий конструктор за замовчуванням, необхідний для JPA.
     * Встановлює {@code lastUpdated} на поточний час.
     */
    public Product() {
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Конструктор для створення нового об'єкта товару.
     * Встановлює {@code lastUpdated} на поточний час.
     *
     * @param code Унікальний код товару.
     * @param article Артикул товару.
     * @param name Назва товару.
     * @param manufacturer Виробник товару.
     * @param unitOfMeasurement Одиниця виміру.
     * @param manufacturerArticle Артикул виробника.
     * @param description Опис товару.
     * @param quantity Поточна кількість на складі.
     * @param reservedQuantity Зарезервована кількість.
     * @param price Ціна товару.
     * @param vatRate Ставка ПДВ.
     * @param category Категорія товару.
     * @param location Місце зберігання товару.
     */
    public Product(String code, String article, String name, String manufacturer, String unitOfMeasurement,
                   String manufacturerArticle, String description, Integer quantity, Integer reservedQuantity,
                   BigDecimal price, BigDecimal vatRate, Category category, Location location) {
        this.code = code;
        this.article = article;
        this.name = name;
        this.manufacturer = manufacturer;
        this.unitOfMeasurement = unitOfMeasurement;
        this.manufacturerArticle = manufacturerArticle;
        this.description = description;
        this.quantity = quantity;
        this.reservedQuantity = reservedQuantity;
        this.price = price;
        this.lastUpdated = LocalDateTime.now();
        this.vatRate = vatRate;
        this.category = category;
        this.location = location;
    }

    /**
     * Повертає ідентифікатор товару.
     * @return Ідентифікатор товару.
     */
    public Long getId() {
        return id;
    }

    /**
     * Встановлює ідентифікатор товару.
     * @param id Новий ідентифікатор товару.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Повертає код товару.
     * @return Код товару.
     */
    public String getCode() {
        return code;
    }

    /**
     * Встановлює код товару.
     * @param code Новий код товару.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Повертає артикул товару.
     * @return Артикул товару.
     */
    public String getArticle() {
        return article;
    }

    /**
     * Встановлює артикул товару.
     * @param article Новий артикул товару.
     */
    public void setArticle(String article) {
        this.article = article;
    }

    /**
     * Повертає назву товару.
     * @return Назва товару.
     */
    public String getName() {
        return name;
    }

    /**
     * Встановлює назву товару.
     * @param name Нова назва товару.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Повертає виробника товару.
     * @return Виробник товару.
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Встановлює виробника товару.
     * @param manufacturer Новий виробник товару.
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Повертає одиницю виміру товару.
     * @return Одиниця виміру товару.
     */
    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    /**
     * Встановлює одиницю виміру товару.
     * @param unitOfMeasurement Нова одиниця виміру товару.
     */
    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    /**
     * Повертає артикул виробника товару.
     * @return Артикул виробника товару.
     */
    public String getManufacturerArticle() {
        return manufacturerArticle;
    }

    /**
     * Встановлює артикул виробника товару.
     * @param manufacturerArticle Новий артикул виробника товару.
     */
    public void setManufacturerArticle(String manufacturerArticle) {
        this.manufacturerArticle = manufacturerArticle;
    }

    /**
     * Повертає опис товару.
     * @return Опис товару.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Встановлює опис товару.
     * @param description Новий опис товару.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Повертає поточну загальну кількість товару на складі.
     * @return Кількість товару.
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Встановлює поточну загальну кількість товару на складі.
     * @param quantity Нова кількість товару.
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Повертає кількість товару, яка була зарезервована.
     * @return Зарезервована кількість товару.
     */
    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    /**
     * Встановлює кількість товару, яка була зарезервована.
     * @param reservedQuantity Нова зарезервована кількість товару.
     */
    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    /**
     * Повертає ціну товару.
     * @return Ціна товару.
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Встановлює ціну товару.
     * @param price Нова ціна товару.
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Повертає дату та час останнього оновлення інформації про товар.
     * @return Дата та час останнього оновлення.
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Встановлює дату та час останнього оновлення інформації про товар.
     * @param lastUpdated Нова дата та час останнього оновлення.
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Повертає ставку ПДВ для товару.
     * @return Ставка ПДВ.
     */
    public BigDecimal getVatRate() {
        return vatRate;
    }

    /**
     * Встановлює ставку ПДВ для товару.
     * @param vatRate Нова ставка ПДВ.
     */
    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    /**
     * Повертає об'єкт категорії, до якої належить товар.
     * @return Об'єкт {@link Category}.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Встановлює категорію товару.
     * @param category Новий об'єкт {@link Category}.
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Повертає об'єкт місця зберігання, де знаходиться товар.
     * @return Об'єкт {@link Location}.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Встановлює місце зберігання товару.
     * @param location Новий об'єкт {@link Location}.
     */
    public void setLocation(Location location) {
        this.location = location;
    }
}