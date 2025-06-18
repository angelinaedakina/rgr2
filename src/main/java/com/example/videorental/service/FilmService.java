package com.example.videorental.service;

import com.example.videorental.model.Film;
import com.example.videorental.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Сервис для работы с фильмами
public class FilmService {

    private final FilmRepository filmRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository; // Внедрение репозитория
    }

    // Получить все фильмы
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    // Получить фильм по ID
    public Optional<Film> getFilmById(Long id) {
        return filmRepository.findById(id);
    }

    // Найти фильмы по названию (без учета регистра)
    public List<Film> getFilmsByTitle(String title) {
        return filmRepository.findByTitleContainingIgnoreCase(title);
    }

    // Создать новый фильм
    public Film createFilm(Film film) {
        return filmRepository.save(film);
    }

    // Обновить данные фильма по ID
    public Film updateFilm(Long id, Film filmDetails) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film not found")); // Если фильм не найден
        film.setTitle(filmDetails.getTitle());
        film.setDirector(filmDetails.getDirector());
        film.setGenre(filmDetails.getGenre());
        film.setCopies(filmDetails.getCopies());
        film.setRentalPrice(filmDetails.getRentalPrice());
        return filmRepository.save(film);
    }

    // Удалить фильм по ID
    public void deleteFilm(Long id) {
        filmRepository.deleteById(id);
    }
}
