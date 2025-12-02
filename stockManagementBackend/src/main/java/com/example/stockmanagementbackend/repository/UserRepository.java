package com.example.stockmanagementbackend.repository;


import com.example.stockmanagementbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторій для роботи з сутністю {@link User}.
 * Надає стандартні механізми для виконання операцій CRUD (Create, Read, Update, Delete)
 * з об'єктами користувачів у базі даних, а також спеціальний метод для пошуку за іменем користувача.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Знаходить користувача за його унікальним ім'ям користувача (логіном).
     * Spring Data JPA автоматично генерує реалізацію цього методу на основі його назви.
     *
     * @param username Ім'я користувача, яке потрібно знайти.
     * @return Об'єкт {@link Optional}, що містить {@link User}, якщо користувач знайдений,
     * або порожній {@link Optional}, якщо користувач з таким ім'ям не існує.
     */
    Optional<User> findByUsername(String username);
}