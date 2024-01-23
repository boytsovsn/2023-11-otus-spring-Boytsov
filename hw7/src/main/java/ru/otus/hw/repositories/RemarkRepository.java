package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.otus.hw.models.Remark;

import java.util.Optional;

public interface RemarkRepository extends CrudRepository<Remark, Long> {

    Optional<Remark> findById(Long id);

    Remark save(Remark remark);

    void deleteById(Long id);
}
