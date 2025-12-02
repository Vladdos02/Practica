package com.example.stockmanagementbackend.repository;

import com.example.stockmanagementbackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторій для роботи з сутністю {@link Product}.
 * Надає механізми для виконання операцій CRUD (Create, Read, Update, Delete)
 * та інших запитів до бази даних для об'єктів {@link Product}, включаючи складні пошукові запити.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Знаходить список товарів, артикул яких містить заданий рядок (без урахування регістру).
     * @param article Частина артикулу для пошуку.
     * @return Список знайдених {@link Product}.
     */
    List<Product> findByArticleContainingIgnoreCase(String article);

    /**
     * Знаходить список товарів, назва яких містить заданий рядок (без урахування регістру).
     * @param name Частина назви для пошуку.
     * @return Список знайдених {@link Product}.
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Знаходить список товарів, виробник яких містить заданий рядок (без урахування регістру).
     * @param manufacturer Частина імені виробника для пошуку.
     * @return Список знайдених {@link Product}.
     */
    List<Product> findByManufacturerContainingIgnoreCase(String manufacturer);

    /**
     * Виконує універсальний пошук товарів за кількома полями (артикул, назва, виробник, код).
     * Пошук є нечутливим до регістру і шукає входження підрядка.
     * Використовує кастомний JPQL-запит.
     *
     * @param query Рядок запиту для пошуку.
     * @return Список знайдених {@link Product}, які відповідають критеріям пошуку.
     */
    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.article) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.manufacturer) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.code) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Product> searchProducts(@Param("query") String query);

    /**
     * Знаходить список товарів за назвою категорії, яка містить заданий рядок (без урахування регістру).
     * Зверніть увагу, що цей метод дозволяє шукати за полем пов'язаної сутності ({@code Category.name}).
     * @param categoryName Частина назви категорії для пошуку.
     * @return Список знайдених {@link Product}.
     */
    List<Product> findByCategoryNameContainingIgnoreCase(String categoryName);

    /**
     * Знаходить список товарів за назвою місця зберігання, яка містить заданий рядок (без урахування регістру).
     * Зверніть увагу, що цей метод дозволяє шукати за полем пов'язаної сутності ({@code Location.name}).
     * @param locationName Частина назви місця зберігання для пошуку.
     * @return Список знайдених {@link Product}.
     */
    List<Product> findByLocationNameContainingIgnoreCase(String locationName);
}