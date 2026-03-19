package com.example.stockmanagementbackend.controller;

import com.example.stockmanagementbackend.dto.ReservationRequest;
import com.example.stockmanagementbackend.dto.ReservationResponseDTO;
import com.example.stockmanagementbackend.model.Reservation;
import com.example.stockmanagementbackend.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST контролер для управління резерваціями товарів.
 * Оновлено: обробка помилок винесена в GlobalExceptionHandler для підтримки Trace ID.
 */
@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:63342")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final ReservationService reservationService;

    /**
     * Конструктор для впровадження залежностей.
     * @param reservationService Сервіс для роботи з резерваціями.
     */
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Створює нову резервацію продукту.
     * @param request Об'єкт {@link ReservationRequest} з деталями замовлення.
     * @return {@link ReservationResponseDTO} з HTTP статусом 201 CREATED.
     */
    @PostMapping("/reserve")
    public ResponseEntity<ReservationResponseDTO> reserveProduct(@RequestBody ReservationRequest request) {
        // Логування входу в ендпоінт. Trace ID додасться автоматично з MDC.
        logger.info("API: Запит на створення резервації для товару ID: {}", request.getProductId());

        Reservation reservation = reservationService.createReservation(
                request.getProductId(),
                request.getUserId(),
                request.getQuantity()
        );

        return new ResponseEntity<>(new ReservationResponseDTO(reservation), HttpStatus.CREATED);
    }

    /**
     * Підтверджує (виконує) існуючу резервацію за її ID.
     * @param id Ідентифікатор резервації.
     * @return Оновлена резервація зі статусом FULFILLED.
     */
    @PutMapping("/{id}/fulfill")
    public ResponseEntity<ReservationResponseDTO> fulfillReservation(@PathVariable Long id) {
        logger.info("API: Запит на виконання резервації ID: {}", id);

        Reservation fulfilledReservation = reservationService.fulfillReservation(id);
        return ResponseEntity.ok(new ReservationResponseDTO(fulfilledReservation));
    }

    /**
     * Скасовує існуючу резервацію за її ID.
     * @param id Ідентифікатор резервації.
     * @return Оновлена резервація зі статусом CANCELED.
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponseDTO> cancelReservation(@PathVariable Long id) {
        logger.info("API: Запит на скасування резервації ID: {}", id);

        Reservation canceledReservation = reservationService.cancelReservation(id);
        return ResponseEntity.ok(new ReservationResponseDTO(canceledReservation));
    }

    /**
     * Отримує список усіх резервацій.
     * @return Список {@link ReservationResponseDTO}.
     */
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        logger.info("API: Отримання списку всіх резервацій");

        List<Reservation> reservations = reservationService.getAllReservations();
        List<ReservationResponseDTO> dtos = reservations.stream()
                .map(ReservationResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Отримує резервацію за її ідентифікатором.
     * @param id Ідентифікатор резервації.
     * @return {@link ReservationResponseDTO} або 404 через GlobalExceptionHandler.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        logger.info("API: Пошук резервації за ID: {}", id);

        return reservationService.getReservationById(id)
                .map(ReservationResponseDTO::new)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new com.example.stockmanagementbackend.exception.ResourceNotFoundException("Reservation not found with id " + id));
    }
}