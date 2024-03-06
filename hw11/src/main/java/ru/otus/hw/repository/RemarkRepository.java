package ru.otus.hw.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.hw.domain.entities.Remark;

public interface RemarkRepository extends ReactiveMongoRepository<Remark, String> {

}
