package com.example.videorental.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "rental")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID аренды

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "film_id", nullable = false)
    @JsonIgnoreProperties({"rentals", "hibernateLazyInitializer", "handler"})
    private Film film; // Фильм

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnoreProperties({"rentals", "hibernateLazyInitializer", "handler"})
    private Customer customer; // Клиент

    @NotNull
    private LocalDateTime rentalStart; // Начало аренды

    @NotNull
    private LocalDateTime rentalDue; // Срок сдачи

    private LocalDateTime rentalReturn; // Дата возврата (null, если не возвращено)

    @NotNull
    @Enumerated(EnumType.STRING)
    private RentalStatus status; // Статус аренды

    public enum RentalStatus {
        ACTIVE,     // активная
        COMPLETED,  // завершена
        OVERDUE     // просрочена
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getRentalStart() {
        return rentalStart;
    }

    public void setRentalStart(LocalDateTime rentalStart) {
        this.rentalStart = rentalStart;
    }

    public LocalDateTime getRentalDue() {
        return rentalDue;
    }

    public void setRentalDue(LocalDateTime rentalDue) {
        this.rentalDue = rentalDue;
    }

    public LocalDateTime getRentalReturn() {
        return rentalReturn;
    }

    public void setRentalReturn(LocalDateTime rentalReturn) {
        this.rentalReturn = rentalReturn;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }
}