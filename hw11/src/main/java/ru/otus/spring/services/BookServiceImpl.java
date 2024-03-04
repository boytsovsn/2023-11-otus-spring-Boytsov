package ru.otus.spring.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.entities.Book;
import ru.otus.spring.domain.entities.Remark;
import ru.otus.spring.exceptions.EntityNotFoundException;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final RemarkService remarkServiceImpl;

    @Override
    public Mono<Book> findById(String id) {
        var book = bookRepository.findById(id);
        return book;
    }

    @Override
    public Flux<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public Mono<Book> insert(String title, String authorId, String genreId) {
        return save(null, title, authorId, genreId);
    }

    @Transactional
    @Override
    public Mono<Book> update(String id, String title, String authorId, String genreId) {
        return save(id, title, authorId, genreId);
    }

    @Override
    public void deleteById(String id) {
//        for (Remark remark: remarkServiceImpl.findByBookId(id).collectList().blockOptional().get())
//            remarkServiceImpl.deleteById(remark.getId());
        bookRepository.deleteById(id);
    }

    private Mono<Book> save(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId).blockOptional()
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId).blockOptional()
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));
        Book book;
        if (id!=null && !id.isEmpty() && !id.equalsIgnoreCase("0")) {
            List<Remark> remarks = null; //remarkServiceImpl.findByBookId(id).collectList().blockOptional().get();
            book = new Book(id, title, author, genre, remarks);
        } else {
           book = new Book(title, author, genre);
        }
        return bookRepository.save(book);
    }
}
