package com.example.videorental.validation;

// Интерфейс для групп валидации
public interface ValidationGroups {

    // Группа для создания (валидация на создание)
    interface OnCreate {}

    // Группа для обновления (валидация на обновление)
    interface OnUpdate {}
}
