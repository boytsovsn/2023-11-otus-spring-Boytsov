package ru.otus.spring.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.entities.Book;
import ru.otus.spring.domain.entities.Remark;
import ru.otus.spring.exceptions.EntityNotFoundException;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.RemarkRepository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RemarkServiceImpl implements RemarkService {

    private final RemarkRepository remarkRepository;

    private final BookRepository bookRepository;

//    @Override
//    @Transactional(readOnly=true)
//    public Flux<Remark> findByBookId(String id) {
////        Mono<Book> book = bookRepository.findById(id);//.orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
////        book.getRemarks().size();
//        return bookRepository.findById(id).map(Optional::of).defaultIfEmpty(Optional.empty())
//                .flatMap(x -> {
//                    if (x.isPresent()) {
//                        return Flux.from(x.get().getRemarks()).flatMap();}
//                    else {
//                        return Flux.empty();}
//                });
//    }

    @Override
    public Mono<Remark> findById(String id) {
        return remarkRepository.findById(id);
    }

    @Transactional
    @Override
    public Mono<Remark> save(String id, String remarkText, String bookId) {
        Remark remark;
        Mono<Remark> savedRemark;
        Book book = bookRepository.findById(bookId).blockOptional().orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        if (id==null || id.isEmpty() || id.equals("0")) {
            remark = new Remark(remarkText, book);
            savedRemark = remarkRepository.save(remark);
            book.getRemarks().add(savedRemark.block(Duration.ofSeconds(1L)));
        } else {
            remark = remarkRepository.findById(id).blockOptional().orElseThrow(() -> new EntityNotFoundException("Remark with id %s not found".formatted(id)));
            remark.setRemarkText(remarkText);
            Book oldBook  = remark.getBook();
            remark.setBook(book);
            savedRemark = remarkRepository.save(remark);
            List<Remark> newRemarks = new ArrayList<>();
            if (oldBook.getId().equals(book.getId())) {              // Книга в комментарии не изменилась
                //Заменить remark в book
                for (Remark bRemark : book.getRemarks()) {
                    if (!bRemark.getId().equals(savedRemark.block(Duration.ofSeconds(1L)).getId()))
                        newRemarks.add(bRemark);
                    else
                        newRemarks.add(savedRemark.block(Duration.ofSeconds(1L)));
                }
                book.setRemarks(newRemarks);
            } else {                                                  // Книга в комментарии изменилась
                //Удалить remark в oldBook
                for (Remark bRemark : oldBook.getRemarks()) {
                    if (!bRemark.getId().equals(savedRemark.block(Duration.ofSeconds(1L)).getId()))
                        newRemarks.add(bRemark);
                }
                oldBook.setRemarks(newRemarks);
                bookRepository.save(oldBook);
                //Добавить remark в book
                book.getRemarks().add(savedRemark.block(Duration.ofSeconds(1L)));
            }
        }

        bookRepository.save(book);
        return savedRemark;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        var remark = remarkRepository.findById(id).blockOptional().orElseThrow(() -> new EntityNotFoundException("Remark with id %s not found".formatted(id)));
        var rbook = remark.getBook();
        var book = bookRepository.findById(rbook.getId()).blockOptional().orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(rbook.getId())));
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
