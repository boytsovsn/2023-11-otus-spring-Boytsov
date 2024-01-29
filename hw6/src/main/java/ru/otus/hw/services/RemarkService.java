package ru.otus.hw.services;

import ru.otus.hw.models.Remark;

import java.util.List;
import java.util.Optional;

public interface RemarkService {

    List<Remark> findByBookId(long id);

    Optional<Remark> findById(long id);

    Remark save(long id, String remarkText, long bookId);

    void deleteById(long id);
}
