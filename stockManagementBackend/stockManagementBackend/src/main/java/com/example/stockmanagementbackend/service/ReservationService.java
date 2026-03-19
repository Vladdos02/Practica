package com.example.stockmanagementbackend.service;

import com.example.stockmanagementbackend.exception.InsufficientStockException;
import com.example.stockmanagementbackend.exception.ResourceNotFoundException;
import com.example.stockmanagementbackend.model.Product;
import com.example.stockmanagementbackend.model.Reservation;
import com.example.stockmanagementbackend.model.User;
import com.example.stockmanagementbackend.repository.ProductRepository;
import com.example.stockmanagementbackend.repository.ReservationRepository;
import com.example.stockmanagementbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервіс для управління операціями, пов'язаними з резерваціями товарів.
 * Реалізує логування критичних операцій, контекстну обробку помилок та
 * гарантує цілісність даних через транзакції.
 */
@Service
public class ReservationService {

    // Ініціалізація логера SLF4J для запису подій системи
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ProductRepository productRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * Створює нову резервацію з перевіркою доступності залишків.
     * Логує параметри запиту для забезпечення контексту при виникненні помилок.
     */
    @Transactional
    public Reservation createReservation(Long productId, Long userId, Integer quantityToReserve) {
        // Контекстне логування вхідного запиту
        logger.info("START: Спроба створення резервації. ProductID={}, UserID={}, Кількість={}",
                productId, userId, quantityToReserve);

        // 1. Пошук товару та логування відсутності ресурсу (404)
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    logger.error("NOT_FOUND: Товар з ID {} не знайдено", productId);
                    return new ResourceNotFoundException("Product not found with id " + productId);
                });

        // 2. Перевірка вільного залишку (Бізнес-логіка)
        int availableStock = product.getQuantity() - product.getReservedQuantity();
        if (availableStock < quantityToReserve) {
            // Логування рівня WARN для очікуваної бізнес-помилки
            logger.warn("INSUFFICIENT_STOCK: Недостатньо товару '{}' (ID: {}). Доступно: {}, Запитано: {}",
                    product.getName(), productId, availableStock, quantityToReserve);
            throw new InsufficientStockException("Not enough available stock for product: " + product.getName() +
                    ". Available: " + availableStock + ", Requested: " + quantityToReserve);
        }

        // 3. Пошук користувача
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("NOT_FOUND: Користувача з ID {} не знайдено", userId);
                    return new ResourceNotFoundException("User not found with id " + userId);
                });

        // 4. Оновлення даних (Транзакційна операція)
        try {
            product.setReservedQuantity(product.getReservedQuantity() + quantityToReserve);
            product.setLastUpdated(LocalDateTime.now());
            productRepository.save(product);

            Reservation reservation = new Reservation();
            reservation.setProduct(product);
            reservation.setUser(user);
            reservation.setQuantity(quantityToReserve);
            reservation.setStatus(Reservation.ReservationStatus.PENDING);
            reservation.setReservationDate(LocalDateTime.now());

            Reservation savedReservation = reservationRepository.save(reservation);

            // Логування успішного завершення операції
            logger.info("SUCCESS: Резервацію успішно створено. ID: {}", savedReservation.getId());
            return savedReservation;
        } catch (Exception e) {
            // Логування системної помилки (Database/Transactional)
            logger.error("SYSTEM_ERROR: Збій при збереженні резервації для ProductID={}. Причина: {}",
                    productId, e.getMessage());
            throw e;
        }
    }

    /**
     * Виконує резервацію (списання товару).
     * Перевіряє статус та узгодженість кількостей.
     */
    @Transactional
    public Reservation fulfillReservation(Long reservationId) {
        logger.info("EXECUTE: Запит на виконання резервації ID: {}", reservationId);

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> {
                    logger.error("NOT_FOUND: Резервацію ID {} не знайдено", reservationId);
                    return new ResourceNotFoundException("Reservation not found with id " + reservationId);
                });

        // Перевірка стану (Тільки PENDING)
        if (reservation.getStatus() != Reservation.ReservationStatus.PENDING) {
            logger.error("INVALID_STATUS: Неможливо виконати резервацію ID {}. Поточний статус: {}",
                    reservationId, reservation.getStatus());
            throw new IllegalStateException("Reservation is not in PENDING status.");
        }

        Product product = reservation.getProduct();

        // Валідація зарезервованої кількості для запобігання від'ємним залишкам
        if (product.getReservedQuantity() < reservation.getQuantity()) {
            logger.error("CONSISTENCY_ERROR: Зарезервована кількість продукту '{}' менша за кількість у резервації ID {}",
                    product.getName(), reservationId);
            throw new IllegalStateException("Reserved quantity consistency error.");
        }

        product.setQuantity(product.getQuantity() - reservation.getQuantity());
        product.setReservedQuantity(product.getReservedQuantity() - reservation.getQuantity());
        product.setLastUpdated(LocalDateTime.now());
        productRepository.save(product);

        reservation.setStatus(Reservation.ReservationStatus.FULFILLED);
        logger.info("SUCCESS: Резервацію ID {} виконано. Товар списано зі складу.", reservationId);
        return reservationRepository.save(reservation);
    }

    /**
     * Скасовує резервацію та повертає товар у вільний запас.
     */
    @Transactional
    public Reservation cancelReservation(Long reservationId) {
        logger.info("CANCEL: Запит на скасування резервації ID: {}", reservationId);

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + reservationId));

        if (reservation.getStatus() != Reservation.ReservationStatus.PENDING) {
            logger.warn("CANCEL_SKIPPED: Резервація ID {} вже має статус {}", reservationId, reservation.getStatus());
            throw new IllegalStateException("Only PENDING reservations can be canceled.");
        }

        Product product = reservation.getProduct();

        // Повернення товару з безпечним відніманням
        int newReservedQuantity = Math.max(0, product.getReservedQuantity() - reservation.getQuantity());
        product.setReservedQuantity(newReservedQuantity);
        product.setLastUpdated(LocalDateTime.now());
        productRepository.save(product);

        reservation.setStatus(Reservation.ReservationStatus.CANCELED);
        logger.info("SUCCESS: Резервацію ID {} скасовано. Товар повернуто у вільний запас.", reservationId);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }
}