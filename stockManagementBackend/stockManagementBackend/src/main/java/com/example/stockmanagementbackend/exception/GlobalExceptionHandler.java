package com.example.stockmanagementbackend.exception;

import com.example.stockmanagementbackend.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice // Дозволяє перехоплювати винятки по всьому застосунку
public class GlobalExceptionHandler {
    // Ініціалізація логера SLF4J
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Обробка специфічної бізнес-помилки (Недостатньо товару)
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStock(InsufficientStockException ex, WebRequest request) {
        ErrorResponse errorRes = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        // Логуємо як WARN, оскільки це очікувана бізнес-ситуація
        logger.warn("[ID:{}] Бізнес-відмова: {}", errorRes.getErrorId(), ex.getMessage());
        return new ResponseEntity<>(errorRes, HttpStatus.BAD_REQUEST);
    }

    // Загальний обробник для непередбачених помилок (500 Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorRes = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Виникла внутрішня помилка системи. Зверніться до підтримки.",
                request.getDescription(false).replace("uri=", "")
        );
        // Логуємо повний стек помилки (ERROR) разом з ID для діагностики
        logger.error("[ID:{}] Критична помилка сервера: ", errorRes.getErrorId(), ex);
        return new ResponseEntity<>(errorRes, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}