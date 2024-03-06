package ru.otus.hw.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.domain.Notes;


public interface NotesRepository extends ReactiveMongoRepository<Notes, String> {

    Flux<Notes> findByPersonId(@NotNull String personId);
}
