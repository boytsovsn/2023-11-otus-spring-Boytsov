package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Remark;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.RemarkRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RemarkServiceImpl implements RemarkService {

    private final RemarkRepository remarkRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly=true)
    public List<Remark> findByBookId(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
        book.getRemarks().size();
        return book.getRemarks();
    }

    @Override
    public Optional<Remark> findById(Long id) {

        return remarkRepository.findById(id);
    }

    @Transactional
    @Override
    public Remark save(Long id, String remarkText, Long bookId) {
        Remark remark;
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        if (id == null || id==0L)
            remark = new Remark(0L, remarkText, book);
        else {
            remark = remarkRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
            remark.setRemarkText(remarkText);
            remark.setBook(book);
        }
        return remarkRepository.save(remark);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        remarkRepository.deleteById(id);
    }
}
