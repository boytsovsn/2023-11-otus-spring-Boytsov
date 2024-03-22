package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.entities.User;
import ru.otus.hw.repositories.UserRepository;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public User findUserByUsername(String userName) {
        return userRepository.findAll().stream().filter(u->u.getLogin().equals(userName)).findFirst().orElseThrow(()->new UsernameNotFoundException("User not found!"));
    }
}
