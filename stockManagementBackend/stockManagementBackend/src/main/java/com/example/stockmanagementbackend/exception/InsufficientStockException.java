package com.example.stockmanagementbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Виняток, що вказує на недостатню кількість товару на складі для виконання операції.
 * При виникненні цього винятку Spring автоматично повертає HTTP статус 400 Bad Request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientStockException extends RuntimeException {

  /**
   * Конструктор для створення InsufficientStockException з вказаним повідомленням.
   *
   * @param message Детальне повідомлення, що пояснює причину винятку (наприклад, "Недостатньо одиниць товару X на складі").
   */
  public InsufficientStockException(String message) {
    super(message);
  }
}