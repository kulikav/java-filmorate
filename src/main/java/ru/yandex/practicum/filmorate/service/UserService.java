package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public Collection<User> getFriends(Long userId) {
        User user = userStorage.findById(userId);
        return user.getFriends().stream()
                .map(userStorage::findById)
                .collect(java.util.stream.Collectors.toList());
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        User user = userStorage.findById(userId);
        User other = userStorage.findById(otherId);

        return user.getFriends().stream()
                .filter(other.getFriends()::contains)
                .map(userStorage::findById)
                .collect(java.util.stream.Collectors.toList());
    }

    public User findById(Long id) {
        return userStorage.findById(id);
    }

}