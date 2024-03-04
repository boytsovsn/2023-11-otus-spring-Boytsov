package ru.otus.spring.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.spring.domain.entities.Remark;

public interface RemarkRepository extends ReactiveMongoRepository<Remark, String> {

}
