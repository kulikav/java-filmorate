package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmsStorage;
    private final UserStorage userStorage;

    public Collection<Film> findAll() {
        return filmsStorage.findAll();
    }

    public Film create(Film film) {
        return filmsStorage.create(film);
    }

    public Film update(Film newFilm) {
        return filmsStorage.update(newFilm);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmsStorage.findById(filmId);
        userStorage.findById(userId);// проверяем, что пользователь существует
        film.getLikes().add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmsStorage.findById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("Лайк от пользователя " + userId + " не найден");
        }
        film.getLikes().remove(userId);
    }

    public Collection<Film> getPopular(int count) {
        return filmsStorage.findAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findById(Long id) {
        return filmsStorage.findById(id);
    }

}
