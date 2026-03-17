package com.example.stockmanagementbackend.controller;

import com.example.stockmanagementbackend.service.ProductService;
import com.example.stockmanagementbackend.dto.ProductResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * REST контролер для управління товарами.
 * Надає ендпоінти для CRUD-операцій та пошуку товарів.
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:63342")
public class ProductController {

    private final ProductService productService;

    /**
     * Конструктор для впровадження залежностей.
     * @param productService Сервіс для роботи з товарами.
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Отримує список усіх товарів.
     * @return Список {@link ProductResponseDTO} з HTTP статусом 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Отримує товар за його ідентифікатором.
     * @param id Ідентифікатор товару.
     * @return {@link ProductResponseDTO} з HTTP статусом 200 OK, або 404 Not Found, якщо товар не знайдено.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(productDto -> new ResponseEntity<>(productDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Шукає товари за запитом у різних полях (назва, артикул, виробник, код).
     * @param query Рядок для пошуку. Необов'язковий.
     * @return Список {@link ProductResponseDTO} з HTTP статусом 200 OK.
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(@RequestParam(required = false) String query) {
        List<ProductResponseDTO> products = productService.searchProducts(query);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Отримує список товарів за частиною назви (без урахування регістру).
     * @param name Частина назви для пошуку.
     * @return Список {@link ProductResponseDTO} з HTTP статусом 200 OK.
     */
    @GetMapping("/by-name")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByName(@RequestParam String name) {
        List<ProductResponseDTO> products = productService.getProductsByName(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Отримує список товарів за частиною артикулу (без урахування регістру).
     * @param article Частина артикулу для пошуку.
     * @return Список {@link ProductResponseDTO} з HTTP статусом 200 OK.
     */
    @GetMapping("/by-article")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByArticle(@RequestParam String article) {
        List<ProductResponseDTO> products = productService.getProductsByArticle(article);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Отримує список товарів за частиною назви категорії (без урахування регістру).
     * @param categoryName Частина назви категорії для пошуку.
     * @return Список {@link ProductResponseDTO} з HTTP статусом 200 OK.
     */
    @GetMapping("/by-category")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@RequestParam String categoryName) {
        List<ProductResponseDTO> products = productService.getProductsByCategoryName(categoryName);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}