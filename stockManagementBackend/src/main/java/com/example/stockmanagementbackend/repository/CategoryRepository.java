package com.example.stockmanagementbackend.repository;

import com.example.stockmanagementbackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторій для роботи з сутністю {@link Category}.
 * Надає механізми для виконання операцій CRUD (Create, Read, Update, Delete)
 * та інших запитів до бази даних для об'єктів {@link Category}.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Знаходить категорію за її унікальним ім'ям.
     * Spring Data JPA автоматично генерує реалізацію цього методу
     * на основі його назви.
     *
     * @param name Назва категорії, яку потрібно знайти.
     * @return Об'єкт {@link Optional}, що містить {@link Category}, якщо вона знайдена,
     * або порожній {@link Optional}, якщо категорія з таким ім'ям не існує.
     */
    Optional<Category> findByName(String name);
}