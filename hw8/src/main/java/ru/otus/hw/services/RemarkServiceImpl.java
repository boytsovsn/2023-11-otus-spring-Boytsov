package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Remark;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.RemarkRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RemarkServiceImpl implements RemarkService {

    private final RemarkRepository remarkRepository;

    private final BookRepository bookRepository;

//    @PersistenceContext
//    private final EntityManager em;

    @Override
    @Transactional(readOnly=true)
    public List<Remark> findByBookId(String id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
        book.getRemarks().size();
        return book.getRemarks();
    }

    @Override
    public Optional<Remark> findById(String id) {

        return remarkRepository.findById(id);
    }

    @Transactional
    @Override
    public Remark save(String id, String remarkText, String bookId) {
        Remark remark;
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        if (id==null || id.isEmpty() || id=="0")
            remark = new Remark(remarkText, bookId);
        else {
            remark = remarkRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
            remark.setRemarkText(remarkText);
        }
        return remarkRepository.save(remark);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        remarkRepository.deleteById(id);
    }
}
