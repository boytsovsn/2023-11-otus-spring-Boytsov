package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.entities.Author;
import ru.otus.hw.repository.AuthorRepository;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public Flux<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Mono<Author> findById(String id) {
        return authorRepository.findById(id);
    }
}
