package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.BaseTest;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Remark;
import ru.otus.hw.repositories.RemarkRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.models", "ru.otus.hw.repositories", "ru.otus.hw.services"})
@DisplayName("Сервисы для Book")
class BookServiceImplTest extends BaseTest {

    @Autowired
    BookService bookServiceImpl;

    @Autowired
    RemarkRepository remarkRepository;

    @BeforeEach
    public void setUp() {
        testN = 3; //(4 тест, если считать с 0)
        super.setUp(testN);
    }

    @DisplayName("Поиск книг по id")
    @Test
    void findById() {
        for (Book checkBook: dbBooks) {
            Book book = bookServiceImpl.findById(checkBook.getId());
            assertThat(book.getId())
                    .isEqualTo(checkBook.getId());
        }
    }

    @DisplayName("Список всех книг")
    @Test
    void findAll() {
        // Список всех книг из БД
        List<Book> books = bookServiceImpl.findAll().stream().sorted(Comparator.comparingLong(Book::getId)).toList();
        // Список тестовых книг из набора для 4го теста  (индекс 3 в списке)
        List<Book> checkBooks = dbBooks;
        int k = 0;
        // Цикл по списку объектов для 4-го теста (индекс 3 в списке)
        for (int i = testN * dbBooks.size(); i < (testN + 1) * dbBooks.size(); i++) {
            assertThat(books.get(i).getId()).isEqualTo(checkBooks.get(k).getId());
            k++;
        }
    }

    @DisplayName("Вставка книги")
    @Test
    void insert() {
        var newBook = new Book("BookTitle_10500", dbAuthors.get(0), dbGenres.get(1));
        var returnedBook = bookServiceImpl.insert(newBook.getTitle(), dbAuthors.get(0).getId(), dbGenres.get(1).getId());
        newBook.setId(returnedBook.getId());
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null && book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(newBook);
        // Отзывы о книге в книге сохраняются в сервисе RemarkService, поэтому проверку по комментариям не делаем
        Book checkBook = bookServiceImpl.findById(returnedBook.getId());
        assertThat(checkBook.getId())
                .isEqualTo(returnedBook.getId());
    }

    @DisplayName("Обновление книги")
    @Test
    void update() {
        int updatedBookN = 1;
        int fromBookN = 2;
        // dbRemarks - список - списков (список по книгам списков комментариев)
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
                .isNotEqualTo(expectedBook);

        var returnedBook = bookServiceImpl.update(expectedBook.getId(), expectedBook.getTitle(), expectedBook.getAuthor().getId(), expectedBook.getGenre().getId());
        // Отзывы о книге в книге сохраняются в сервисе RemarkService, поэтому мы здесь комментарии просто
        // присваиваем, чтобы книги стали равны
        returnedBook.setRemarks(expectedRemarks);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null && book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
        // Восстанавливаем изменённую книгу
        bookServiceImpl.update(expectedBook.getId(), dbBooks.get(updatedBookN-1).getTitle(), dbAuthors.get(updatedBookN-1).getId(), dbGenres.get(updatedBookN-1).getId());
    }

    @DisplayName("Удаление книги")
    @Test
    void deleteById() {
        Long deletedBookId = dbBooks.get(2).getId();
        Assertions.assertThrows(EntityNotFoundException.class, () -> {bookServiceImpl.findById(deletedBookId);});
        List<Remark> expectedRemarks = bookServiceImpl.findAll().stream().filter(x->x.getId()==deletedBookId).flatMap(x->x.getRemarks().stream()).toList();
        bookServiceImpl.deleteById(deletedBookId);
        Throwable exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {bookServiceImpl.findById(deletedBookId);});
        assertEquals("Book with id %s not found".formatted(deletedBookId), exception.getMessage());
        for (Remark expectedRemark: expectedRemarks) {
            var remark = remarkRepository.findById(expectedRemark.getId());
            assertThat(remark).isEmpty();
        }
    }
}