package ru.otus.hw.services;

import ru.otus.hw.models.entities.Remark;

import java.util.List;
import java.util.Optional;

public interface RemarkService {

    List<Remark> findByBookId(String id);

    Optional<Remark> findById(String id);

    Remark save(String id, String remarkText, String bookId);

    void deleteById(String id);
}
