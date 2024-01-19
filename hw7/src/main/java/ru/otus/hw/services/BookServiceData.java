package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.repositories.RemarkRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceData implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final RemarkRepository remarkRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(long id) {
        var book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        var author = authorRepository.findById(book.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(book.getAuthorId())));;
        book.setAuthor(author);
        var genre = genreRepository.findById(book.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(book.getGenreId())));
        book.setGenre(genre);
        var remarks = remarkRepository.findByBookId(book.getId());
        book.getRemarks().size();
        return Optional.of(book);
    }

    @Transactional
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
    @Transactional
    @Override
    public Book insert(String title, long authorId, long genreId) {
        return save(0, title, authorId, genreId);
    }
    @Transactional
    @Override
    public Book update(long id, String title, long authorId, long genreId) {
        return save(id, title, authorId, genreId);
    }
    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var remarks = remarkRepository.findByBookId(id);
        var book = new Book(id, title, authorId, author, genreId, genre, remarks);
        return bookRepository.save(book);
    }
}
