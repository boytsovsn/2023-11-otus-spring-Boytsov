package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Remark;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final RemarkService remarkServiceImpl;

    @Override
    public Optional<Book> findById(String id) {
        var book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
        var author = authorRepository.findById(book.getAuthor().getId()).orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(book.getAuthor().getId())));;
        book.setAuthor(author);
        var genre = genreRepository.findById(book.getGenre().getId())
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(book.getGenre().getId())));
        book.setGenre(genre);
        var remarks = remarkServiceImpl.findByBookId(id);
        book.setRemarks(remarks);
        return Optional.of(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public Book insert(String title, String authorId, String genreId) {
        return save(null, title, authorId, genreId);
    }

    @Transactional
    @Override
    public Book update(String id, String title, String authorId, String genreId) {
        return save(id, title, authorId, genreId);
    }

    @Override
    public void deleteById(String id) {
        for (Remark remark: remarkServiceImpl.findByBookId(id))
            remarkServiceImpl.deleteById(remark.getId());
        bookRepository.deleteById(id);
    }

    private Book save(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));
        Book book;
        if (id!=null && !id.isEmpty() && !id.equalsIgnoreCase("0")) {
            var remarks = remarkServiceImpl.findByBookId(id);
            book = new Book(id, title, author, genre, remarks);
        } else {
           book = new Book(title, author, genre);
        }
        return bookRepository.save(book);
    }
}
