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
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.hw.BaseTest;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Remark;
import ru.otus.hw.repositories.RemarkRepository;

import java.nio.file.AccessDeniedException;
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
        testN = allEntitiesModelImpl.getTestsCount(); //(номер особых данных теста из предзаписанных данных в БД для тестирования ACL)
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

    @WithMockUser(
            username = "user",
            password = "password",
            roles = {"USER"}
    )
    @DisplayName("Список всех книг для user")
    @Test
    void findAllForUser() {
        // Список всех книг из БД для пользователя user (2 книги)
        List<Book> books = bookServiceImpl.findAll().stream().sorted(Comparator.comparingLong(Book::getId)).toList();
        // Список тестовых книг (3 книги)
        List<Book> checkBooks = dbBooks;
        assertThat(books.get(0).getId()).usingRecursiveComparison().isEqualTo(checkBooks.get(1).getId());
        assertThat(books.get(1).getId()).usingRecursiveComparison().isEqualTo(checkBooks.get(2).getId());
    }

    @WithMockUser(
            username = "admin",
            password = "password",
            roles = {"USER"}
    )
    @DisplayName("Список всех книг для admin")
    @Test
    void findAllForAdmin() {
        // Список всех книг из БД для пользователя админ (3 книги)
        List<Book> books = bookServiceImpl.findAll().stream().sorted(Comparator.comparingLong(Book::getId)).toList();
        // Список тестовых книг (3 книги)
        List<Book> checkBooks = dbBooks;
        assertThat(books.get(0).getId()).usingRecursiveComparison().isEqualTo(checkBooks.get(0).getId());
        assertThat(books.get(1).getId()).usingRecursiveComparison().isEqualTo(checkBooks.get(1).getId());
        assertThat(books.get(2).getId()).usingRecursiveComparison().isEqualTo(checkBooks.get(3).getId());
    }

    @WithMockUser(
            username = "user",
            password = "password",
            roles = {"USER"}
    )
    @DisplayName("Вставка книги для user")
    @Test
    void insertForUser() {
        var newBook = new Book(4L,"BookTitle_10500", dbAuthors.get(0), dbGenres.get(1), null);
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

    @WithMockUser(
            username = "user",
            password = "password",
            roles = {"USER"}
    )
    @DisplayName("Обновление книги")
    @Test
    void updateForUserSuccess() {
        int updatedBookN = 2;
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

        var returnedBook = bookServiceImpl.update(expectedBook);
        // Отзывы о книге в книге сохраняются в сервисе RemarkService, поэтому мы здесь комментарии просто
        // присваиваем, чтобы книги стали равны
        returnedBook.setRemarks(expectedRemarks);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() != null && book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
        // Восстанавливаем изменённую книгу
        bookServiceImpl.update(expectedBook.getId(), dbBooks.get(updatedBookN-1).getTitle(), dbAuthors.get(updatedBookN-1).getId(), dbGenres.get(updatedBookN-1).getId());
    }

    @WithMockUser(
            username = "admin",
            password = "password",
            roles = {"EDITOR"}
    )
    @DisplayName("Обновление книги")
    @Test
    void updateForAdminUnSuccess() {
        int updatedBookN = 3;
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

        Throwable exception = Assertions.assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> {var returnedBook = bookServiceImpl.update(expectedBook);});
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