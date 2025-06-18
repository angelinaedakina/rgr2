package com.example.videorental.service;

import com.example.videorental.model.Customer;
import com.example.videorental.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // Обозначает класс как сервисный компонент
public class CustomerService {

    @Autowired // Автоматически внедряет зависимость репозитория клиента
    private CustomerRepository customerRepository;

    // Метод для получения клиента по email (без учета регистра)
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmailIgnoreCase(email);
    }

    // Дополнительные методы сервиса можно добавить по необходимости
}
