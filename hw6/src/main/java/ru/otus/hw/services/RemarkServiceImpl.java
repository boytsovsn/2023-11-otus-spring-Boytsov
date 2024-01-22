package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Remark> findByBookId(long id) {
        var query = em.createQuery("select r from Remark r left join r.book b where b.id = :id", Remark.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Override
    public Optional<Remark> findById(long id) {

        return remarkRepository.findById(id);
    }

    @Transactional
    @Override
    public Remark save(long id, String remarkText, long bookId) {
        Remark remark;
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        if (id==0)
            remark = new Remark(0, remarkText, book);
        else {
            remark = remarkRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
            remark.setRemarkText(remarkText);
            remark.setBook(book);
        }
        return remarkRepository.save(remark);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        remarkRepository.deleteById(id);
    }
}
