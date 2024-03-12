package ru.otus.hw.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.hw.domain.entities.Book;
import ru.otus.hw.domain.entities.Remark;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.repository"})
class RemarkRepositoryTest {

    @Autowired
    private RemarkRepository repository;

    @Test
    @DisplayName("Проверка реактивного репозитория на вставку/удаления элемента")
    void shouldSetIdOnSaveAndDeleted() {
        Book book = new Book("65e018a2b543be609ff166c3", null, null, null, null);
        Mono<Remark> remarkMono = repository.save(new Remark("Круто! Легко читается!", book.getId()));
        AtomicReference<String> id = new AtomicReference<>("");
        StepVerifier
                .create(remarkMono)
                .assertNext(remark -> {
                    id.set(remark.getId()); assertNotNull(remark.getId());})
                .expectComplete()
                .verify();
        Mono<Void> res =  repository.deleteById(id.get());
        StepVerifier.create(res)
                .verifyComplete();
    }

}
