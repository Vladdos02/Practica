package com.example.stockmanagementbackend.controller;

import com.example.stockmanagementbackend.model.User;
import com.example.stockmanagementbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * REST контролер для обробки запитів аутентифікації користувачів.
 * Дозволяє користувачам входити в систему та отримувати інформацію про себе.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:63342")
public class AuthController {

    private final UserService userService;

    /**
     * Конструктор для ініціалізації сервісу користувачів.
     * @param userService Сервіс для аутентифікації.
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Обробляє запит на вхід користувача.
     * Аутентифікує користувача за ім'ям користувача та паролем.
     *
     * @param loginRequest Карта, що містить "username" та "password" для входу.
     * @return {@code ResponseEntity} з роллю та ID користувача у разі успіху, або 401 Unauthorized у разі невдачі.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        Optional<User> userOptional = userService.authenticate(username, password);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(Map.of(
                    "role", user.getRole(),
                    "userId", user.getId()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    /**
     * Отримує інформацію про користувача за його ім'ям користувача.
     * Призначено для демонстраційних цілей або перевірки даних користувача.
     *
     * @param username Ім'я користувача для пошуку.
     * @return {@code ResponseEntity} з об'єктом {@link User} у разі знаходження, або 404 Not Found.
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}