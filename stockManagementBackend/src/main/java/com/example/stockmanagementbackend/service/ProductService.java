package com.example.stockmanagementbackend.service;

import com.example.stockmanagementbackend.model.Product;
import com.example.stockmanagementbackend.repository.CategoryRepository;
import com.example.stockmanagementbackend.repository.LocationRepository;
import com.example.stockmanagementbackend.repository.ProductRepository;
import com.example.stockmanagementbackend.dto.ProductResponseDTO;
import com.example.stockmanagementbackend.exception.ResourceNotFoundException; // Імпортуємо виняток
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервіс для управління операціями, пов'язаними з товарами ({@link Product}).
 * Надає методи для отримання, створення, оновлення, видалення та пошуку товарів,
 * а також взаємодіє з репозиторіями {@link ProductRepository}, {@link CategoryRepository}
 * та {@link LocationRepository}.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    /**
     * Конструктор для впровадження залежностей (dependency injection) репозиторіїв.
     * @param productRepository Репозиторій для товарів.
     * @param categoryRepository Репозиторій для категорій.
     * @param locationRepository Репозиторій для місць зберігання.
     */
    @Autowired
    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          LocationRepository locationRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
    }

    /**
     * Допоміжна функція для мапінгу об'єкта {@link Product} моделі
     * на об'єкт {@link ProductResponseDTO}.
     * @param product Об'єкт {@link Product}, який потрібно мапувати.
     * @return Відповідний {@link ProductResponseDTO}.
     */
    private ProductResponseDTO mapToDto(Product product) {
        return new ProductResponseDTO(product);
    }

    /**
     * Отримує список всіх товарів у системі.
     * @return Список {@link ProductResponseDTO}, що представляють всі товари.
     */
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Отримує товар за його ідентифікатором.
     * @param id Ідентифікатор товару.
     * @return {@link Optional}, що містить {@link ProductResponseDTO}, якщо товар знайдено,
     * або порожній {@link Optional}, якщо товар не існує.
     */
    public Optional<ProductResponseDTO> getProductById(Long id) {
        return productRepository.findById(id).map(this::mapToDto);
    }

    /**
     * Виконує універсальний пошук товарів за заданим рядком.
     * Якщо запит порожній або null, повертає всі товари.
     * @param query Рядок запиту для пошуку (за артикулом, назвою, виробником, кодом).
     * @return Список {@link ProductResponseDTO}, що відповідають критеріям пошуку.
     */
    public List<ProductResponseDTO> searchProducts(String query) {
        List<Product> products;
        if (query == null || query.trim().isEmpty()) {
            products = productRepository.findAll();
        } else {
            products = productRepository.searchProducts(query);
        }
        return products.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



    /**
     * Отримує список товарів за частиною назви (без урахування регістру).
     * @param name Частина назви товару для пошуку.
     * @return Список {@link ProductResponseDTO}, що відповідають критеріям пошуку.
     */
    public List<ProductResponseDTO> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Отримує список товарів за частиною артикулу (без урахування регістру).
     * @param article Частина артикулу товару для пошуку.
     * @return Список {@link ProductResponseDTO}, що відповідають критеріям пошуку.
     */
    public List<ProductResponseDTO> getProductsByArticle(String article) {
        return productRepository.findByArticleContainingIgnoreCase(article).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Отримує список товарів за частиною назви категорії (без урахування регістру).
     * @param categoryName Частина назви категорії для пошуку.
     * @return Список {@link ProductResponseDTO}, що відповідають критеріям пошуку.
     */
    public List<ProductResponseDTO> getProductsByCategoryName(String categoryName) {
        return productRepository.findByCategoryNameContainingIgnoreCase(categoryName).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}