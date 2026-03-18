package com.example.stockmanagementbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.DisplayName;

@SpringBootTest
class StockManagementBackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("Документація сценарію: Успішне резервування при достатній кількості товару")
    void testSuccessfulReservationFlow() {
        // Опис сценарію:
        // 1. Клієнт обирає товар (Product) з наявною кількістю 10 одиниць.
        // 2. Клієнт створює запит на резервування 3 одиниць.
        // 3. Система повинна змінити 'reservedQuantity' з 0 на 3.
        // 4. Доступний залишок (quantity - reservedQuantity) стає рівним 7.

        // Тут має бути виклик сервісу:
        // reservationService.createReservation(productId, userId, 3);

        // Перевірка результату:
        // assertEquals(3, updatedProduct.getReservedQuantity());
    }
}
