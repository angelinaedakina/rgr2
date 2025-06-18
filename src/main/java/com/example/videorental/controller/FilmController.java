package com.example.videorental.controller;

import com.example.videorental.model.Film;
import com.example.videorental.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/film")
public class FilmController {

    @Autowired
    private FilmRepository filmRepository;

    // Получить список всех фильмов (доступ USER, ADMIN)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<Film>> getAllFilms() {
        return new ResponseEntity<>(filmRepository.findAll(), HttpStatus.OK);
    }

    // Получить фильм по ID (доступ USER, ADMIN)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Long id) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film not found with id: " + id));
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    // Найти фильмы по названию (доступ USER, ADMIN)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/title/{title}")
    public ResponseEntity<List<Film>> getFilmsByTitle(@PathVariable String title) {
        List<Film> films = filmRepository.findByTitleContainingIgnoreCase(title);
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

    // Создать новый фильм (доступ только ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<Film> createFilm(@RequestBody Film film) {
        Film savedFilm = filmRepository.save(film);
        return new ResponseEntity<>(savedFilm, HttpStatus.CREATED);
    }

    // Обновить данные фильма (доступ только ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable Long id, @RequestBody Film filmDetails) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film not found with id: " + id));
        film.setTitle(filmDetails.getTitle());
        film.setDirector(filmDetails.getDirector());
        film.setGenre(filmDetails.getGenre());
        film.setCopies(filmDetails.getCopies());
        film.setRentalPrice(filmDetails.getRentalPrice());
        Film updatedFilm = filmRepository.save(film);
        return new ResponseEntity<>(updatedFilm, HttpStatus.OK);
    }

    // Удалить фильм (доступ только ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Long id) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film not found with id: " + id));
        filmRepository.delete(film);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
