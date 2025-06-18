package com.example.videorental.repository;

import com.example.videorental.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    // Поиск фильмов по части названия (без учета регистра)
    List<Film> findByTitleContainingIgnoreCase(String title);

    // Получение популярных фильмов (сортировка по количеству выдач)
    @Query("SELECT f FROM Film f LEFT JOIN f.rentals r GROUP BY f.id ORDER BY COUNT(r) DESC")
    List<Film> findPopularFilms();
}