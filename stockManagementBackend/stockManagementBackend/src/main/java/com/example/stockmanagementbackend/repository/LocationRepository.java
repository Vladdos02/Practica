package com.example.stockmanagementbackend.repository;

import com.example.stockmanagementbackend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторій для роботи з сутністю {@link Location}.
 * Надає механізми для виконання операцій CRUD (Create, Read, Update, Delete)
 * та інших запитів до бази даних для об'єктів {@link Location}.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    /**
     * Знаходить місце зберігання за його унікальним ім'ям.
     * Spring Data JPA автоматично генерує реалізацію цього методу
     * на основі його назви.
     *
     * @param name Назва місця зберігання, яку потрібно знайти.
     * @return Об'єкт {@link Optional}, що містить {@link Location}, якщо воно знайдено,
     * або порожній {@link Optional}, якщо місце зберігання з таким ім'ям не існує.
     */
    Optional<Location> findByName(String name);
}