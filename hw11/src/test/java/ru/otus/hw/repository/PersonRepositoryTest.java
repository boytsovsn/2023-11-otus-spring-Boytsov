package ru.otus.hw.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.domain.Person;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.changelogs.test", "ru.otus.hw.repository"})
class PersonRepositoryTest {

    @Autowired
    private PersonRepository repository;

    @Test
    void shouldSetIdOnSave() {
        Mono<Person> personMono = repository.save(new Person("Bill", 12));

        StepVerifier
                .create(personMono)
                .assertNext(person -> assertNotNull(person.getId()))
                .expectComplete()
                .verify();
    }
}
