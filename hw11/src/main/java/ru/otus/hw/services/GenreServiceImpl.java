package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.entities.Author;
import ru.otus.hw.domain.entities.Genre;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Flux<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Flux<Genre> saveAll(List<Genre> genres) {
        return genreRepository.saveAll(genres);
    };

    @Override
    public Mono<Genre> findById(String id) {
        return genreRepository.findById(id);
    }
}
