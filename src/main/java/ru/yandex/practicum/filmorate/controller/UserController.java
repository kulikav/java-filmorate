package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new ConcurrentHashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        // проверяем выполнение необходимых условий
        validate(user);
        // формируем дополнительные данные
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    private void validate(User user) {
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
    }

    // вспомогательный метод для генерации идентификатора
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        // проверяем необходимые условия
        validate(newUser);
        if (exists(newUser)) {
            users.put(newUser.getId(), newUser);
            return newUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private boolean exists(User user) {
        return users.containsKey(user.getId());
    }

}
