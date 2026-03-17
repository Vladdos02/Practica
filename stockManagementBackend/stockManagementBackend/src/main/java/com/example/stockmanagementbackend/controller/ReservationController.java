package com.example.stockmanagementbackend.controller;

import com.example.stockmanagementbackend.dto.ErrorResponse;
import com.example.stockmanagementbackend.dto.ReservationRequest;
import com.example.stockmanagementbackend.dto.ReservationResponseDTO;
import com.example.stockmanagementbackend.exception.InsufficientStockException;
import com.example.stockmanagementbackend.exception.ResourceNotFoundException;
import com.example.stockmanagementbackend.model.Reservation;
import com.example.stockmanagementbackend.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST контролер для управління резерваціями товарів.
 * Надає ендпоінти для створення, виконання, скасування та перегляду резервацій.
 */
@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:63342")
public class ReservationController {

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
     * @param request Об'єкт {@link ReservationRequest}, що містить ID продукту, ID користувача та кількість для резервування.
     * @param webRequest Об'єкт {@link WebRequest} для отримання деталей запиту в разі помилки.
     * @return {@link ReservationResponseDTO} з HTTP статусом 201 CREATED у разі успіху,
     * або {@link ErrorResponse} з відповідним статусом (400, 404, 500) у разі помилки.
     */
    @PostMapping("/reserve")
    public ResponseEntity<?> reserveProduct(@RequestBody ReservationRequest request, WebRequest webRequest) {
        try {
            Reservation reservation = reservationService.createReservation(
                    request.getProductId(),
                    request.getUserId(),
                    request.getQuantity()
            );
            return new ResponseEntity<>(new ReservationResponseDTO(reservation), HttpStatus.CREATED);
        } catch (InsufficientStockException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            webRequest.getDescription(false).replace("uri=", "")
                    ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            webRequest.getDescription(false).replace("uri=", "")
                    ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            "Error reserving product: " + e.getMessage(),
                            webRequest.getDescription(false).replace("uri=", "")
                    ));
        }
    }

    /**
     * Підтверджує (виконує) існуючу резервацію за її ID.
     * Після виконання кількість продукту на складі зменшується.
     * @param id Ідентифікатор резервації для виконання.
     * @param webRequest Об'єкт {@link WebRequest} для отримання деталей запиту в разі помилки.
     * @return {@link ReservationResponseDTO} з HTTP статусом 200 OK у разі успіху,
     * або {@link ErrorResponse} з відповідним статусом (400, 404, 500) у разі помилки.
     */
    @PutMapping("/{id}/fulfill")
    public ResponseEntity<?> fulfillReservation(@PathVariable Long id, WebRequest webRequest) {
        try {
            Reservation fulfilledReservation = reservationService.fulfillReservation(id);
            return ResponseEntity.ok(new ReservationResponseDTO(fulfilledReservation));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            webRequest.getDescription(false).replace("uri=", "")
                    ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            webRequest.getDescription(false).replace("uri=", "")
                    ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            "Error fulfilling reservation: " + e.getMessage(),
                            webRequest.getDescription(false).replace("uri=", "")
                    ));
        }
    }

    /**
     * Скасовує існуючу резервацію за її ID.
     * Після скасування зарезервована кількість продукту повертається на склад.
     * @param id Ідентифікатор резервації для скасування.
     * @param webRequest Об'єкт {@link WebRequest} для отримання деталей запиту в разі помилки.
     * @return {@link ReservationResponseDTO} з HTTP статусом 200 OK у разі успіху,
     * або {@link ErrorResponse} з відповідним статусом (400, 404, 500) у разі помилки.
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id, WebRequest webRequest) {
        try {
            Reservation canceledReservation = reservationService.cancelReservation(id);
            return ResponseEntity.ok(new ReservationResponseDTO(canceledReservation));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            e.getMessage(),
                            webRequest.getDescription(false).replace("uri=", "")
                    ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            webRequest.getDescription(false).replace("uri=", "")
                    ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            "Error canceling reservation: " + e.getMessage(),
                            webRequest.getDescription(false).replace("uri=", "")
                    ));
        }
    }

    /**
     * Отримує список усіх резервацій.
     * @return Список {@link ReservationResponseDTO} з HTTP статусом 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        List<ReservationResponseDTO> dtos = reservations.stream()
                .map(ReservationResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Отримує резервацію за її ідентифікатором.
     * @param id Ідентифікатор резервації.
     * @return {@link ReservationResponseDTO} з HTTP статусом 200 OK, або 404 Not Found, якщо резервацію не знайдено.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(ReservationResponseDTO::new)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}