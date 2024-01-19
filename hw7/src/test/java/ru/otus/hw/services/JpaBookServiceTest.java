package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Remark;
import ru.otus.hw.repositories.AuthorRepositoryJpa;
import ru.otus.hw.repositories.BookRepositoryJpa;
import ru.otus.hw.repositories.GenreRepositoryJpa;
import ru.otus.hw.repositories.RemarkRepositoryJpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@DisplayName("JPA сервис для Book")
@DataJpaTest
@Import({BookServiceImpl.class, BookRepositoryJpa.class, AuthorRepositoryJpa.class, GenreRepositoryJpa.class, RemarkServiceImpl.class, RemarkRepositoryJpa.class})
@Transactional(propagation = Propagation.NEVER)
class JpaBookServiceTest {

    @Autowired
    private BookService bookServiceImpl;

    @Autowired
    private RemarkService remarkServiceImpl;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<List<Remark>> dbRemarks;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbRemarks = getDbRemarks();
        dbBooks = getDbBooks(dbAuthors, dbGenres, dbRemarks);
    }

    @DisplayName("Список всех книг")
    @Test
    void findAll() {
        List<Book> books = bookServiceImpl.findAll();
        List<Book> checkBooks = dbBooks;
        assertThat(books.size()).isEqualTo(checkBooks.size());
        for (int i = 0; i < books.size(); i++) {
            assertThat(books.get(i).getId()).isEqualTo(checkBooks.get(i).getId());
            assertThat(books.get(i).getAuthorId()).isEqualTo(checkBooks.get(i).getAuthorId());
            assertThat(books.get(i).getAuthor()).isEqualTo(checkBooks.get(i).getAuthor());
            assertThat(books.get(i).getGenreId()).isEqualTo(checkBooks.get(i).getGenreId());
            assertThat(books.get(i).getGenre()).isEqualTo(checkBooks.get(i).getGenre());
            assertThat(books.get(i).getTitle()).isEqualTo(checkBooks.get(i).getTitle());
            assertThat(books.get(i).getRemarks().size()).isEqualTo(checkBooks.get(i).getRemarks().size());
            for (int j = 0; j < books.get(i).getRemarks().size(); j++) {
                assertThat(books.get(i).getRemarks().get(j)).isEqualTo(checkBooks.get(i).getRemarks().get(j));
            }
        }
//        assertThat(books).containsExactlyElementsOf(checkBooks);    //не разобрался почему не работает!
        books.forEach(System.out::println);
    }

    @DisplayName("Загрузка книги по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void findById(Book checkBook) {
        var book = bookServiceImpl.findById(checkBook.getId()).get();
        assertThat(book.getId()).isEqualTo(checkBook.getId());
        assertThat(book.getAuthorId()).isEqualTo(checkBook.getAuthorId());
        assertThat(book.getAuthor()).isEqualTo(checkBook.getAuthor());
        assertThat(book.getGenreId()).isEqualTo(checkBook.getGenreId());
        assertThat(book.getGenre()).isEqualTo(checkBook.getGenre());
        assertThat(book.getTitle()).isEqualTo(checkBook.getTitle());
        assertThat(book.getRemarks().size()).isEqualTo(checkBook.getRemarks().size());
        for (int j = 0; j < book.getRemarks().size(); j++) {
            assertThat(book.getRemarks().get(j)).isEqualTo(checkBook.getRemarks().get(j));
        }
//        assertThat(book).isPresent()    //тоже самое
//                .get()
//                .isEqualTo(checkBook);
    }

    @DisplayName("Cохранение новой книги")
    @Test
    void insertBook() {
        var newBook = new Book(0, "BookTitle_10500", dbAuthors.get(0).getId(), dbAuthors.get(0), dbGenres.get(1).getId(), dbGenres.get(1), new ArrayList<Remark>());
        var returnedBook = bookServiceImpl.insert("BookTitle_10500", dbAuthors.get(0).getId(), dbGenres.get(1).getId());
        newBook.setId(returnedBook.getId());
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(newBook);
        Remark newRemark = new Remark(0, "Это особое мнение", returnedBook.getId());
        var returnedRemark = remarkServiceImpl.save(0, "Это особое мнение", returnedBook.getId());
        returnedBook.setRemarks(Arrays.asList(returnedRemark));
        Optional<Book> checkBook = bookServiceImpl.findById(returnedBook.getId());
        assertThat(checkBook)
                .isPresent()
                .get()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(returnedBook);
    }

    @DisplayName("Обновление книги")
    @Test
    void updatedBook() {
        long updatedBookId = 1L;
        int fromBookIndex = 2;
        List<Remark> expectedRemarks = dbRemarks.get((int) (updatedBookId-1));
        int i = 0;
        for (Remark expRemark : expectedRemarks) {
            if (i >= dbRemarks.get(fromBookIndex).size())
                break;
            expRemark.setRemarkText(dbRemarks.get(fromBookIndex).get(i).getRemarkText());
            i++;
        }
        var expectedBook = new Book(updatedBookId, "BookTitle_10500", dbAuthors.get(fromBookIndex).getId(), dbAuthors.get(fromBookIndex), dbGenres.get(fromBookIndex).getId(), dbGenres.get(fromBookIndex), expectedRemarks);

        assertThat(bookServiceImpl.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookServiceImpl.update(updatedBookId, "BookTitle_10500", dbAuthors.get(fromBookIndex).getId(), dbGenres.get(fromBookIndex).getId());
        returnedBook.setRemarks(expectedRemarks);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        for (Remark remark : expectedRemarks) {
            var returnedRemark = remarkServiceImpl.save(remark.getId(), remark.getRemarkText(), returnedBook.getId());
        }
        assertThat(bookServiceImpl.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(returnedBook);
    }

    @DisplayName("Удаление книги по id")
    @Test
    void deleteBook() {
        long deletedBookId = 3L;
        assertThat(bookServiceImpl.findById(deletedBookId)).isPresent();
        bookServiceImpl.deleteById(deletedBookId);
        Throwable exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bookServiceImpl.findById(deletedBookId);});
        assertEquals("findById error!", exception.getMessage(), "Book with id %s not found".formatted(deletedBookId));
        Assert.isTrue(remarkServiceImpl.findByBookId(deletedBookId).isEmpty(), "Remarks is not deleted for book id %d".formatted(deletedBookId));
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<List<Remark>> getDbRemarks() {
        return IntStream.range(1, 4).boxed()
                .map(id -> IntStream.range(1, id+1).boxed()
                        .map(id1 ->new Remark(id*(id-1)/2+id1,"Remark_"+id+id1, id)).toList())
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> _dbAuthors, List<Genre> _dbGenres, List<List<Remark>> _dbRemarks) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id, "BookTitle_" + id, _dbAuthors.get(id - 1).getId(), _dbAuthors.get(id - 1), _dbGenres.get(id - 1).getId(), _dbGenres.get(id - 1), _dbRemarks.get(id-1)))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var authors = getDbAuthors();
        var genres = getDbGenres();
        var remarks = getDbRemarks();
        return getDbBooks(authors, genres, remarks);
    }
}