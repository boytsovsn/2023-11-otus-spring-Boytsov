package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.Assert;
import ru.otus.hw.BaseTest;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Remark;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.changelogs.test", "ru.otus.hw.repositories"})
@DisplayName("Mongo репозиторий для Book")
class BookRepositoryTest extends BaseTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RemarkRepository RemarkRepository;



    @DisplayName("Список всех книг")
    @Test
    void findAll() {
        List<Book> books = bookRepository.findAll();
        List<Book> checkBooks = dbBooks;
        assertThat(books.size()).isEqualTo(checkBooks.size());
        for (int i = 0; i < books.size(); i++) {
            assertThat(books.get(i).getId()).isEqualTo(checkBooks.get(i).getId());
            assertThat(books.get(i).getAuthor().getId()).isEqualTo(checkBooks.get(i).getAuthor().getId());
            assertThat(books.get(i).getGenre().getId()).isEqualTo(checkBooks.get(i).getGenre().getId());
            assertThat(books.get(i).getGenre()).isEqualTo(checkBooks.get(i).getGenre());
            assertThat(books.get(i).getTitle()).isEqualTo(checkBooks.get(i).getTitle());
            assertThat(books.get(i).getRemarks().size()).isEqualTo(checkBooks.get(i).getRemarks().size());
            for (int j = 0; j < books.get(i).getRemarks().size(); j++) {
                assertThat(books.get(i).getRemarks().get(j)).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(checkBooks.get(i).getRemarks().get(j));
            }
        }
        books.forEach(System.out::println);
    }

    @DisplayName("Загрузка книги по id")
    @Test
    void findById() {
        for (Book checkBook: dbBooks) {
            var book = bookRepository.findById(checkBook.getId()).get();
            assertThat(book.getId()).isEqualTo(checkBook.getId());
            assertThat(book.getAuthor().getId()).isEqualTo(checkBook.getAuthor().getId());
            assertThat(book.getAuthor()).isEqualTo(checkBook.getAuthor());
            assertThat(book.getGenre().getId()).isEqualTo(checkBook.getGenre().getId());
            assertThat(book.getGenre()).isEqualTo(checkBook.getGenre());
            assertThat(book.getTitle()).isEqualTo(checkBook.getTitle());
            assertThat(book.getRemarks().size()).isEqualTo(checkBook.getRemarks().size());
            for (int j = 0; j < book.getRemarks().size(); j++) {
                assertThat(book.getRemarks().get(j)).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(checkBook.getRemarks().get(j));
            }
        }
    }

    @DisplayName("Cохранение новой книги")
    @Test
    void insertBook() {
        var newBook = new Book("BookTitle_10500", dbAuthors.get(0), dbGenres.get(1));
        var returnedBook = bookRepository.save(newBook);
        newBook.setId(returnedBook.getId());
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null && book.getId().length() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(newBook);
        Remark newRemark = new Remark( "Это особое мнение", returnedBook);
        var returnedRemark = RemarkRepository.save(newRemark);
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
                .matches(book -> book.getId() != null && book.getId().length() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        for (Remark remark : expectedRemarks) {
            var returnedRemark = RemarkRepository.save(remark);
        }
        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("Удаление книги по id")
    @Test
    void deleteBook() {
        String deletedBookId = dbBooks.get(2).getId();
        assertThat(bookRepository.findById(deletedBookId)).isPresent();
        List<Remark> expectedRemarks = bookRepository.findById(deletedBookId).get().getRemarks();
        bookRepository.deleteById(deletedBookId);
        Assert.isTrue(bookRepository.findById(deletedBookId).isEmpty(), "Book with id = %s is not deleted".formatted(deletedBookId));
    }
}