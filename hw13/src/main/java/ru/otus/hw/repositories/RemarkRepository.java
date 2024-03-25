package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.models.entities.Remark;

public interface RemarkRepository extends CrudRepository<Remark, Long> {

}
