package ru.otus.hw.services;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.BaseTest;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Remark;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.changelogs.test", "ru.otus.hw.repositories", "ru.otus.hw.services"})
@DisplayName("Сервисы для Remark")
class RemarkServiceImplTest extends BaseTest {

    @Autowired
    private RemarkService remarkServiceImpl;

    @Autowired
    private BookRepository bookRepository;

    private static String insertedRemarkId;

    private static String updatedRemarkId;

    private final int insertToBookN = 2;

    private final int updatedRemarkN = 1;

    @BeforeEach
    public void setUp() {
        testN = 1;
        super.setUp(testN);
    }

    @DisplayName("Замечания по id книги")
    @Test
    void findByBookId() {
        for (int i = 0; i < dbRemarks.size(); i++) {
            List<Remark> actualRemarks = remarkServiceImpl.findByBookId(dbRemarks.get(i).get(0).getBook().getId());
            List<Remark> expectedRemarks = dbRemarks.get(i);
                assertThat(actualRemarks).isEqualTo(expectedRemarks);
        }
    }

    @DisplayName("Замечание по id")
    @Test
    void findById() {
        List<Remark> remarks = convertToFlatDbRemarks();
        for (Remark expectedRemark: remarks) {
            var actualRemark = remarkServiceImpl.findById(expectedRemark.getId()).orElseThrow(() -> new EntityNotFoundException("Remark not found, id %s".formatted(expectedRemark.getId())));
                assertThat(actualRemark).isEqualTo(expectedRemark);
        }
    }

    @DisplayName("Вставка замечания")
    @Test
    void insert() {
        Book insertToBook = dbBooks.get(insertToBookN-1);
        var newRemark = new Remark("Remark_10500", insertToBook);
        var returnedRemark = remarkServiceImpl.save(null, "Remark_10500", insertToBook.getId());
        insertedRemarkId = returnedRemark.getId();
        newRemark.setId(returnedRemark.getId());
        assertThat(returnedRemark).isNotNull()
                .matches(remark -> remark.getId() != null && remark.getId().length() > 0)
                .isEqualTo(newRemark);
        Optional<Remark> expectedRemark = remarkServiceImpl.findById(returnedRemark.getId());
        assertThat(expectedRemark)
                .isPresent()
                .get()
                .isEqualTo(returnedRemark);
        remarkServiceImpl.deleteById(insertedRemarkId);
    }

    @DisplayName("Обновление замечания")
    @Test
    void update() {
        int forBookN = 2;
        Book forBook = dbBooks.get(forBookN-1);
        updatedRemarkId = convertToFlatDbRemarks().get(updatedRemarkN-1).getId();
        var updatedRemark = new Remark(updatedRemarkId, "Remark_10500", forBook);
        updatedRemarkId = updatedRemark.getId();
        var fromBDRemark = remarkServiceImpl.findById(updatedRemark.getId());
        assertThat(fromBDRemark)
                .isPresent()
                .get()
                .isNotEqualTo(updatedRemark);

        var returnedRemark = remarkServiceImpl.save(updatedRemarkId, "Remark_10500", forBook.getId());
        assertThat(returnedRemark).isNotNull()
                .matches(remark -> remark.getId() != null && remark.getId().length() > 0)
                .isEqualTo(updatedRemark);

        Remark expectedRemark = remarkServiceImpl.findById(returnedRemark.getId()).get();
        assertThat(expectedRemark.getId())
                .isEqualTo(returnedRemark.getId());
        assertThat(expectedRemark.getRemarkText())
                .isEqualTo(returnedRemark.getRemarkText());
        assertThat(expectedRemark.getBook())
                .isEqualTo(returnedRemark.getBook());
        // Проверка, что замечание "перекочевало" из одной книги в другую
        var bookFrom = bookRepository.findById(fromBDRemark.get().getBook().getId());
        var bookTo   = bookRepository.findById(forBook.getId());
        int i = 0;
        for (Remark remark : bookFrom.get().getRemarks()) {
            if (remark.getId().equals(updatedRemarkId))
                break;
            i++;
        }
        // Замечания нет в книге, из которой  оно переместилось
        assertThat(i).isEqualTo(bookFrom.get().getRemarks().size());

        i = 0;
        for (Remark remark : bookTo.get().getRemarks()) {
            if (remark.getId().equals(updatedRemarkId))
                break;
            i++;
        }
        // Зато есть там, куда переместилось
        assertThat(i).isLessThan(bookTo.get().getRemarks().size());
        //Восстановление изменённого замечания
        remarkServiceImpl.save(updatedRemarkId, fromBDRemark.get().getRemarkText(), fromBDRemark.get().getBook().getId());
    }

    @DisplayName("Удаление замечания")
    @Test
    void deleteById() {
        Book insertToBook = dbBooks.get(insertToBookN-1);
        var returnedRemark = remarkServiceImpl.save(null, "Remark_100500", insertToBook.getId());
        String deletedRemarkId = returnedRemark.getId();
        assertThat(remarkServiceImpl.findById(deletedRemarkId)).isPresent();
        var remark = remarkServiceImpl.findById(deletedRemarkId);
        remarkServiceImpl.deleteById(deletedRemarkId);
        Throwable exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            remarkServiceImpl.findById(deletedRemarkId).orElseThrow(() -> new EntityNotFoundException("Remark with id %s not found".formatted(deletedRemarkId)));});
        assertEquals("Remark with id %s not found".formatted(deletedRemarkId), exception.getMessage());
    }
}