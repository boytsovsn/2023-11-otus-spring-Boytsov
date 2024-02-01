package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Remark;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.RemarkRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RemarkServiceImpl implements RemarkService {

    private final RemarkRepository remarkRepository;

    private final BookRepository bookRepository;

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
        Remark savedRemark;
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        if (id==null || id.isEmpty() || id.equals("0")) {
            remark = new Remark(remarkText, bookId);
            savedRemark = remarkRepository.save(remark);
            book.getRemarks().add(savedRemark);
        } else {
            remark = remarkRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Remark with id %s not found".formatted(id)));
            remark.setRemarkText(remarkText);
            savedRemark = remarkRepository.save(remark);
            List<Remark> newRemarks = new ArrayList<>();
            for (Remark bRemark: book.getRemarks()) {
                if (!bRemark.getId().equals(savedRemark.getId()))
                    newRemarks.add(bRemark);
                else
                    newRemarks.add(savedRemark);
            }
            book.setRemarks(newRemarks);
        }

        bookRepository.save(book);
        return savedRemark;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        var remark = remarkRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Remark with id %s not found".formatted(id)));
        String bookId = remark.getBookId();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        List<Remark> newRemarks = new ArrayList<>();
        for (Remark bRemark: book.getRemarks()) {
            if (!bRemark.getId().equals(id)) {
                newRemarks.add(bRemark);
            }
        }
        book.setRemarks(newRemarks);
        bookRepository.save(book);
        remarkRepository.deleteById(id);
    }
}
