package com.example.stockmanagementbackend.service;

import com.example.stockmanagementbackend.model.User;
import com.example.stockmanagementbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Сервіс для управління операціями, пов'язаними з користувачами.
 * Включає пошук, збереження та (спрощену) аутентифікацію користувачів.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    /**
     * Конструктор для впровадження залежності {@link UserRepository}.
     *
     * @param userRepository Репозиторій для роботи з сутністю користувача.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Знаходить користувача за його ім'ям користувача (логіном).
     *
     * @param username Ім'я користувача, яке потрібно знайти.
     * @return Об'єкт {@link Optional}, що містить {@link User}, якщо користувача знайдено,
     * або порожній {@link Optional}, якщо користувач з таким ім'ям не існує.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Зберігає об'єкт користувача в базі даних.
     *
     * @param user Об'єкт {@link User}, який потрібно зберегти.
     * @return Збережений об'єкт {@link User}.
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Дуже спрощений метод для "аутентифікації" користувача.
     * @param username Ім'я користувача для аутентифікації.
     * @param password Пароль для аутентифікації (у відкритому вигляді).
     * @return Об'єкт {@link Optional}, що містить {@link User}, якщо аутентифікація успішна,
     * або порожній {@link Optional}, якщо аутентифікація невдала (користувача не знайдено або пароль невірний).
     */
    public Optional<User> authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}