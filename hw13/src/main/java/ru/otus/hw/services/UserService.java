package ru.otus.hw.services;

import ru.otus.hw.models.entities.User;

public interface UserService {

    User findUserByUsername(String userName);
}
