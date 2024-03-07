package ru.otus.hw.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.domain.entities.Book;
import ru.otus.hw.domain.entities.Remark;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.changelogs.test", "ru.otus.hw.repository"})
class RemarkRepositoryTest {

    @Autowired
    private RemarkRepository repository;

    @Test
    void shouldSetIdOnSave() {
        Mono<Remark> remarkMono = repository.save(new Remark("Круто! Легко читается!", new Book("65e018a2b543be609ff166c3", null, null, null, null)));
        StepVerifier
                .create(remarkMono)
                .assertNext(remark -> assertNotNull(remark.getId()))
                .expectComplete()
                .verify();
    }
}
