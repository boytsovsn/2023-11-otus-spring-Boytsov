package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Remark;

@Repository
public interface RemarkRepositoryData extends CrudRepository<Remark, Long> {

}
