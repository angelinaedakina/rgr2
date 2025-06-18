package com.example.videorental.repository;

import com.example.videorental.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Поиск клиентов по ФИО (без учета регистра)
    List<Customer> findByFullNameContainingIgnoreCase(String fullName);

    // Поиск клиента по email (без учета регистра)
    Customer findByEmailIgnoreCase(String email);

    // Клиенты-должники (выдачи ACTIVE или OVERDUE >= 2)
    @Query("SELECT c FROM Customer c WHERE (SELECT COUNT(r) FROM Rental r WHERE r.customer = c AND r.status IN ('ACTIVE','OVERDUE')) >= 2")
    List<Customer> findDebtors();
}
