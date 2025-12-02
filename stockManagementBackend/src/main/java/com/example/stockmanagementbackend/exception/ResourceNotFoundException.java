package com.example.stockmanagementbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Виняток, що вказує на те, що запитуваний ресурс (наприклад, товар, користувач, резервація)
 * не знайдено в системі.
 * При виникненні цього винятку Spring автоматично повертає HTTP статус 404 Not Found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

  /**
   * Конструктор для створення ResourceNotFoundException з вказаним повідомленням.
   *
   * @param message Детальне повідомлення, що пояснює, який ресурс не знайдено (наприклад, "Товар з ID 123 не знайдено").
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }
}