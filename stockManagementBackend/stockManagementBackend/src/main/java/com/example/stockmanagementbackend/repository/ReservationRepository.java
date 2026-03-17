package com.example.stockmanagementbackend.repository;

import com.example.stockmanagementbackend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторій для роботи з сутністю {@link Reservation}.
 * Надає стандартні механізми для виконання операцій CRUD (Create, Read, Update, Delete)
 * з об'єктами резервацій у базі даних.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Spring Data JPA автоматично надає реалізацію для основних CRUD операцій.
}