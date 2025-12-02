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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервіс для управління операціями, пов'язаними з резерваціями товарів.
 * Включає створення, виконання та скасування резервацій,
 * а також оновлення кількостей товару на складі.
 */
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * Конструктор для впровадження залежностей (dependency injection) репозиторіїв.
     * @param reservationRepository Репозиторій для резервацій.
     * @param productRepository Репозиторій для товарів.
     * @param userRepository Репозиторій для користувачів.
     */
    public ReservationService(ReservationRepository reservationRepository,
                              ProductRepository productRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /**
     * Створює нову резервацію товару.
     * Перед створенням резервації перевіряє наявність товару та користувача,
     * а також достатність доступного запасу.
     * Збільшує зарезервовану кількість товару на складі.
     *
     * @param productId Ідентифікатор товару, який потрібно зарезервувати.
     * @param userId Ідентифікатор користувача, який створює резервацію.
     * @param quantityToReserve Кількість товару для резервування.
     * @return Створений об'єкт {@link Reservation}.
     * @throws ResourceNotFoundException якщо товар або користувач не знайдено.
     * @throws InsufficientStockException якщо на складі недостатньо доступного товару.
     */
    @Transactional
    public Reservation createReservation(Long productId, Long userId, Integer quantityToReserve) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        int availableStock = product.getQuantity() - product.getReservedQuantity();
        if (availableStock < quantityToReserve) {
            throw new InsufficientStockException("Not enough available stock for product: " + product.getName() +
                    ". Available: " + availableStock +
                    ", Requested: " + quantityToReserve);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        product.setReservedQuantity(product.getReservedQuantity() + quantityToReserve);
        product.setLastUpdated(LocalDateTime.now());
        productRepository.save(product);

        Reservation reservation = new Reservation();
        reservation.setProduct(product);
        reservation.setUser(user);
        reservation.setQuantity(quantityToReserve);
        reservation.setStatus(Reservation.ReservationStatus.PENDING);
        reservation.setReservationDate(LocalDateTime.now());

        return reservationRepository.save(reservation);
    }

    /**
     * Виконує резервацію, змінюючи її статус на FULFILLED.
     * Зменшує загальну кількість товару на складі та зарезервовану кількість.
     *
     * @param reservationId Ідентифікатор резервації, яку потрібно виконати.
     * @return Оновлений об'єкт {@link Reservation} зі статусом FULFILLED.
     * @throws ResourceNotFoundException якщо резервацію не знайдено.
     * @throws IllegalStateException якщо резервація не перебуває у статусі PENDING.
     */
    @Transactional
    public Reservation fulfillReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + reservationId));

        if (reservation.getStatus() != Reservation.ReservationStatus.PENDING) {
            throw new IllegalStateException("Reservation is not in PENDING status. Current status: " + reservation.getStatus());
        }

        Product product = reservation.getProduct();

        if (product.getReservedQuantity() < reservation.getQuantity()) {
            throw new IllegalStateException("Reserved quantity on product (" + product.getReservedQuantity() +
                    ") is less than reservation quantity (" + reservation.getQuantity() + ") for reservation ID: " + reservationId);
        }

        product.setQuantity(product.getQuantity() - reservation.getQuantity());
        product.setReservedQuantity(product.getReservedQuantity() - reservation.getQuantity());
        product.setLastUpdated(LocalDateTime.now());
        productRepository.save(product);

        reservation.setStatus(Reservation.ReservationStatus.FULFILLED);
        return reservationRepository.save(reservation);
    }

    /**
     * Скасовує резервацію, змінюючи її статус на CANCELED.
     * Повертає зарезервовану кількість товару назад у доступний запас.
     *
     * @param reservationId Ідентифікатор резервації, яку потрібно скасувати.
     * @return Оновлений об'єкт {@link Reservation} зі статусом CANCELED.
     * @throws ResourceNotFoundException якщо резервацію не знайдено.
     * @throws IllegalStateException якщо резервація не перебуває у статусі PENDING.
     */
    @Transactional
    public Reservation cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + reservationId));


        if (reservation.getStatus() != Reservation.ReservationStatus.PENDING) {
            throw new IllegalStateException("Reservation is not in PENDING status. Current status: " + reservation.getStatus());
        }

        Product product = reservation.getProduct();

        int newReservedQuantity = product.getReservedQuantity() - reservation.getQuantity();
        if (newReservedQuantity < 0) {
            newReservedQuantity = 0;
        }
        product.setReservedQuantity(newReservedQuantity);

        product.setQuantity(product.getQuantity() + reservation.getQuantity());

        product.setLastUpdated(LocalDateTime.now());
        productRepository.save(product);

        reservation.setStatus(Reservation.ReservationStatus.CANCELED);
        return reservationRepository.save(reservation);
    }

    /**
     * Отримує список всіх резервацій у системі.
     * @return Список всіх об'єктів {@link Reservation}.
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    /**
     * Отримує резервацію за її ідентифікатором.
     * @param id Ідентифікатор резервації.
     * @return {@link Optional}, що містить {@link Reservation}, якщо резервацію знайдено,
     * або порожній {@link Optional}, якщо резервація не існує.
     */
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }
}