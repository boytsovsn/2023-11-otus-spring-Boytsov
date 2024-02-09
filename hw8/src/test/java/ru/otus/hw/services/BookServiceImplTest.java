package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.BaseTest;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Remark;
import ru.otus.hw.repositories.RemarkRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.changelogs.test", "ru.otus.hw.repositories", "ru.otus.hw.services"})
@DisplayName("Сервисы для Book")
class BookServiceImplTest extends BaseTest {

    @Autowired
    BookService bookServiceImpl;

    @Autowired
    RemarkRepository remarkRepository;

    @BeforeEach
    public void setUp() {
        testN = 3;
        super.setUp(testN);
    }

    @DisplayName("Поиск книг по id")
    @Test
    void findById() {
        for (Book checkBook: dbBooks) {
            var book = bookServiceImpl.findById(checkBook.getId()).get();
            assertThat(book)
                    .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(checkBook);
        }
    }

    @DisplayName("Список всех книг")
    @Test
    void findAll() {
        List<Book> books = bookServiceImpl.findAll();
        List<Book> checkBooks = dbBooks;
        int k = 0;
        for (int i = testN * dbBooks.size(); i < (testN + 1) * dbBooks.size(); i++) {
            assertThat(books.get(i))
                    .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(checkBooks.get(k));
            k++;
        }
    }

    @DisplayName("Вставка книги")
    @Test
    void insert() {
    }

    @DisplayName("Обновление книги")
    @Test
    void update() {
        int updatedBookN = 1;
        int fromBookN = 2;
        // dbRemarks - список (по книгам) - списков (комментариев по книге)
        List<Remark> expectedRemarks = dbRemarks.get(updatedBookN-1);
        int i = 0;
        for (Remark expRemark : expectedRemarks) {
            if (i >= dbRemarks.get(fromBookN).size())
                break;
            expRemark.setRemarkText(dbRemarks.get(fromBookN).get(i).getRemarkText());
            i++;
        }
        var expectedBook = new Book(dbBooks.get(updatedBookN-1).getId(), "BookTitle_10500", dbAuthors.get(fromBookN), dbGenres.get(fromBookN), expectedRemarks);

        assertThat(bookServiceImpl.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookServiceImpl.update(expectedBook.getId(), expectedBook.getTitle(), expectedBook.getAuthor().getId(), expectedBook.getGenre().getId());
        returnedBook.setRemarks(expectedRemarks);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null && book.getId().length() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        for (Remark remark : expectedRemarks) {
            var returnedRemark = remarkRepository.save(remark);
        }
        assertThat(bookServiceImpl.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("Удаление книги")
    @Test
    void deleteById() {
        String deletedBookId = dbBooks.get(2).getId();
        var book= bookServiceImpl.findById(deletedBookId);
        assertThat(book).isPresent();
        List<Remark> expectedRemarks = book.get().getRemarks();
        bookServiceImpl.deleteById(deletedBookId);
        Throwable exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {bookServiceImpl.findById(deletedBookId);});
        assertEquals("Book with id %s not found".formatted(deletedBookId), exception.getMessage());
        for (Remark expectedRemark: expectedRemarks) {
            var remark = remarkRepository.findById(expectedRemark.getId());
            assertThat(remark).isEmpty();
        }
    }
}