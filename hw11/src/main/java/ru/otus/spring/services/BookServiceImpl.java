package ru.otus.spring.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.domain.entities.Author;
import ru.otus.spring.domain.entities.Book;
import ru.otus.spring.domain.entities.Remark;
import ru.otus.spring.repository.AuthorRepository;
import ru.otus.spring.repository.BookRepository;
import ru.otus.spring.repository.GenreRepository;

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

    @Override
    public Mono<Book> insert(String title, String authorId, String genreId) {
        return save(null, title, authorId, genreId);
    }


    @Override
    public Mono<Book> update(String id, String title, String authorId, String genreId) {
        return save(id, title, authorId, genreId);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        remarkServiceImpl.findByBookId(id).flatMap(x->{deleteRemarkById(x);
            return Mono.empty();
        });
        return bookRepository.deleteById(id);
    }

    public Mono<Void> deleteRemarkById(Remark remark) {
        return remarkServiceImpl.deleteById(remark.getId());

    }

    private Mono<Book> save(String id, String title, String authorId, String genreId) {
        if (id!=null && !id.isEmpty() && !id.equalsIgnoreCase("0")) {
            return bookRepository.findById(id).flatMap((x)->{
                authorRepository.findById(authorId).subscribe(result->x.setAuthor(result));
                genreRepository.findById(genreId).subscribe(result->x.setGenre(result));
                remarkServiceImpl.findByBookId(id).collectList().subscribe(result->x.setRemarks(result));
                x.setTitle(title);
                return bookRepository.save(x);
            });
        } else {
           Mono<Author> author = authorRepository.findById(authorId);
           return Mono.just(new Book()).flatMap((x)->{
               authorRepository.findById(authorId).subscribe(result->x.setAuthor(result));
               genreRepository.findById(genreId).subscribe(result->x.setGenre(result));
               x.setTitle(title);
               return bookRepository.save(x);
           });
        }
    }
}
