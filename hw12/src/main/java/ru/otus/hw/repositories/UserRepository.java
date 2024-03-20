package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.entities.User;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAll();
}
