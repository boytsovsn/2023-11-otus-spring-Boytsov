package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.Assert;
import ru.otus.hw.BaseTest;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Remark;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.models", "ru.otus.hw.repositories"})
@DisplayName("JPA репозиторий для Book")
class BookRepositoryTest extends BaseTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RemarkRepository remarkRepository;

    @BeforeEach
    public void setUp() {
        testN = 0;
        super.setUp(testN);
    }

    @DisplayName("Список всех книг")
    @Test
    void findAll() {
        List<Book> books = bookRepository.findAll();
        List<Book> checkBooks = dbBooks;
        int k = 0;
        for (int i = testN * dbBooks.size(); i < (testN + 1) * dbBooks.size(); i++) {
            assertThat(books.get(i).getId()).isEqualTo(checkBooks.get(k).getId());
            k++;
        }
    }

    @DisplayName("Загрузка книги по id")
    @Test
    void findById() {
        for (Book checkBook: dbBooks) {
            var book = bookRepository.findById(checkBook.getId()).get();
            assertThat(book.getId()).isEqualTo(checkBook.getId());
        }
    }

    @DisplayName("Cохранение новой книги")
    @Test
    void insertBook() {
        var newBook = new Book("BookTitle_10500", dbAuthors.get(0), dbGenres.get(1));
        var returnedBook = bookRepository.save(newBook);
        newBook.setId(returnedBook.getId());
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null && book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(newBook);
        Remark newRemark = new Remark( "Это особое мнение", returnedBook);
        var returnedRemark = remarkRepository.save(newRemark);
        returnedBook.setRemarks(Arrays.asList(returnedRemark));
        bookRepository.save(returnedBook);
        Optional<Book> checkBook = bookRepository.findById(returnedBook.getId());
        assertThat(checkBook)
                .isPresent()
                .get()
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(returnedBook);
    }

    @DisplayName("Обновление книги")
    @Test
    void updatedBook() {
        int updatedBookId = 1;
        int fromBookIndex = 2;
        List<Remark> expectedRemarks = dbRemarks.get(updatedBookId-1);
        int i = 0;
        for (Remark expRemark : expectedRemarks) {
            if (i >= dbRemarks.get(fromBookIndex).size())
                break;
            expRemark.setRemarkText(dbRemarks.get(fromBookIndex).get(i).getRemarkText());
            i++;
        }
        var expectedBook = new Book(dbBooks.get(updatedBookId-1).getId(), "BookTitle_10500", dbAuthors.get(fromBookIndex), dbGenres.get(fromBookIndex), expectedRemarks);

        assertThat(bookRepository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookRepository.save(expectedBook);
        returnedBook.setRemarks(expectedRemarks);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null && book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        for (Remark remark : expectedRemarks) {
            var returnedRemark = remarkRepository.save(remark);
        }
        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("Удаление книги по id")
    @Test
    void deleteBook() {
        Long deletedBookId = dbBooks.get(2).getId();
        assertThat(bookRepository.findById(deletedBookId)).isPresent();

        bookRepository.deleteById(deletedBookId);
        Assert.isTrue(bookRepository.findById(deletedBookId).isEmpty(), "Book with id = %s is not deleted".formatted(deletedBookId));
    }
}