package ru.otus.hw.services;

import ru.otus.hw.models.entities.Remark;

import java.util.List;
import java.util.Optional;

public interface RemarkService {

    List<Remark> findByBookId(Long id);

    Optional<Remark> findById(Long id);

    Remark save(Long id, String remarkText, Long bookId);

    void deleteById(Long id);
}
