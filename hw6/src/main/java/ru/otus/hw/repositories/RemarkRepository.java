package ru.otus.hw.repositories;

import ru.otus.hw.models.Remark;

import java.util.Optional;

public interface RemarkRepository {

    Optional<Remark> findById(long id);

    Remark save(Remark remark);

    void deleteById(long id);
}
