package com.example.videorental.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer") // Определение таблицы "customer"
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоинкрементный первичный ключ
    private Long id;

    private String fullName; // Полное имя клиента

    @Column(unique = true) // Email должен быть уникальным
    private String email;

    private String phone; // Телефон клиента

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Игнорировать поле при сериализации в JSON
    private List<Rental> rentals; // Список арендованных фильмов

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }
}
