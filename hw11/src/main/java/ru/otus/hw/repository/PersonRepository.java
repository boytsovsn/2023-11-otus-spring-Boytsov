package ru.otus.hw.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.domain.Person;

public interface PersonRepository extends ReactiveMongoRepository<Person, String> {

    @NotNull Mono<Person> findById(@NotNull String id);

    Mono<Person> save(Mono<Person> person);

    Flux<Person> findAllByLastName(String lastName);

    Flux<Person> findAllByAge(int age);
}
