package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.entities.Genre;
import ru.otus.hw.repository.GenreRepository;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Flux<Genre> findAll() {
        return genreRepository.findAll();
    }
    @Override
    public Mono<Genre> findById(String id) {
        return genreRepository.findById(id);
    }
}
