package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Remark;

public interface RemarkRepository extends MongoRepository<Remark, String> {

}
