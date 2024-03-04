package ru.otus.hw.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.BaseTest;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Remark;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.changelogs.test", "ru.otus.hw.repositories"})
@DisplayName("Mongo репозиторий для Remark")
class RemarkRepositortyTest extends BaseTest {

    @Autowired
    private RemarkRepository remarkRepository;

    private final int updatedRemarkN = 1;

    private final int deletedRemarkN = 2;

    private final int insertToBookN = 2;

    @BeforeEach
    public void setUp() {
        testN = 0;
        super.setUp(testN);
    }

    @DisplayName("Замечание по id")
    @Test
    void findById() {
        List<Remark> remarksList = convertToFlatDbRemarks();
        String updatedRemarkId = remarksList.get(updatedRemarkN-1).getId();
        String deletedRemarkId = remarksList.get(deletedRemarkN-1).getId();
        for (Remark remark: remarksList) {
                var fRemark = remarkRepository.findById(remark.getId()).orElseThrow(() -> new EntityNotFoundException("Remark not found, id %s".formatted(remark.getId())));
                assertThat(fRemark.getId()).isEqualTo(remark.getId());
                assertThat(fRemark.getRemarkText()).isEqualTo(remark.getRemarkText());
                assertThat(fRemark.getBook()).isEqualTo(remark.getBook());
        }
    }

    @DisplayName("Вставка замечания")
    @Test
    void insertRemark() {
        Book insertToBook = dbBooks.get(insertToBookN-1);
        var newRemark = new Remark("Remark_10500", insertToBook);
        var returnedRemark = remarkRepository.save(newRemark);
        newRemark.setId(returnedRemark.getId());
        assertThat(returnedRemark).isNotNull()
                .matches(remark -> remark.getId() != null && remark.getId().length() > 0)
                .isEqualTo(newRemark);
        Optional<Remark> checkRemark = remarkRepository.findById(returnedRemark.getId());
        assertThat(checkRemark)
                .isPresent()
                .get()
                .isEqualTo(returnedRemark);
        remarkRepository.deleteById(returnedRemark.getId());
    }

    @DisplayName("Обновление замечания")
    @Test
    void updateRemark() {
        int forBookN = 2;
        Book forBook = dbBooks.get(forBookN-1);
        String updatedRemarkId = convertToFlatDbRemarks().get(updatedRemarkN-1).getId();
        var expectedRemark = new Remark(updatedRemarkId, "Remark_10500", forBook);
        var fromBDRemark = remarkRepository.findById(expectedRemark.getId());
        assertThat(fromBDRemark)
                .isPresent()
                .get()
                .isNotEqualTo(expectedRemark);

        var returnedRemark = remarkRepository.save(expectedRemark);
        assertThat(returnedRemark).isNotNull()
                .matches(remark -> remark.getId() != null && remark.getId().length() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields()
                .isEqualTo(expectedRemark);

        Remark bdReamrk = remarkRepository.findById(returnedRemark.getId()).get();
        assertThat(bdReamrk.getId())
                .isEqualTo(returnedRemark.getId());
        assertThat(bdReamrk.getRemarkText())
                .isEqualTo(returnedRemark.getRemarkText());
        assertThat(bdReamrk.getBook())
                .isEqualTo(returnedRemark.getBook());
        remarkRepository.save(fromBDRemark.get());
    }

    @DisplayName("Удаление замечания")
    @Test
    void deleteById() {
        Book insertToBook = dbBooks.get(insertToBookN-1);
        var returnedRemark = remarkRepository.save(new Remark("Remark_100500", insertToBook));
        String deletedRemarkId = returnedRemark.getId();
        assertThat(remarkRepository.findById(deletedRemarkId)).isPresent();
        var remark = remarkRepository.findById(deletedRemarkId);
        remarkRepository.deleteById(deletedRemarkId);
        Throwable exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            remarkRepository.findById(deletedRemarkId).orElseThrow(() -> new EntityNotFoundException("Remark with id %s not found".formatted(deletedRemarkId)));});
        assertEquals("Remark with id %s not found".formatted(deletedRemarkId), exception.getMessage());
    }

}