package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.Remark;

import java.util.List;
import java.util.Optional;

public interface RemarkRepository {

    List<Remark> findByBookId(long id);

    Optional<Remark> findById(long id);

    Remark save(Remark remark);

    void deleteById(long id);
}
