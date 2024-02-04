package ru.otus.hw.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Remark;
import ru.otus.hw.changelogs.test.AllEntitiesModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.changelogs.test", "ru.otus.hw.repositories"})
@DisplayName("Mongo репозиторий для Remark")
class RemarkRepositortyTest {

    @Autowired
    private RemarkRepository remarkRepository;

    @Autowired
    private AllEntitiesModel allEntitiesModelImpl;

    private final int updatedRemarkN = 1;

    private final int deletedRemarkN = 2;
    private List<List<Remark>> remarks;

    @BeforeEach
    void setUp() {
        remarks = getDbRemarks();
    }

    @DisplayName("Замечание по id")
    @Test
    void findById() {
        List<Remark> remarksList = convertToFlatDbRemarks();
        String updatedRemarkId = remarksList.get(updatedRemarkN-1).getId();
        String deletedRemarkId = remarksList.get(deletedRemarkN-1).getId();
        for (Remark remark: remarksList) {
            if (remark.getId() != updatedRemarkId && remark.getId() != deletedRemarkId) {
                var fRemark = remarkRepository.findById(remark.getId()).orElseThrow(() -> new EntityNotFoundException("Remark not found, id %s".formatted(remark.getId())));
                assertThat(fRemark.getId()).isEqualTo(remark.getId());
                assertThat(fRemark.getRemarkText()).isEqualTo(remark.getRemarkText());
                assertThat(fRemark.getBookId()).isEqualTo(remark.getBookId());
            }
        }
    }

    @DisplayName("Вставка замечания")
    @Test
    void insertRemark() {
        int insertToBookN = 2;
        String insertToBookId = allEntitiesModelImpl.getBooks().get(insertToBookN-1).getId();
        Book insertToBook = new Book(insertToBookId, null, null, null, null);
        var newRemark = new Remark("Remark_10500", insertToBookId);
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
        String forBookId = allEntitiesModelImpl.getBooks().get(forBookN-1).getId();
        String updatedRemarkId = allEntitiesModelImpl.getRemarks().get(updatedRemarkN-1).getId();
        var expectedRemark = new Remark(updatedRemarkId, "Remark_10500", forBookId);

        assertThat(remarkRepository.findById(expectedRemark.getId()))
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
        assertThat(bdReamrk.getBookId())
                .isEqualTo(returnedRemark.getBookId());
    }

    @DisplayName("Удаление замечания")
    @Test
    void deleteById() {
        String deletedRemarkId = allEntitiesModelImpl.getRemarks().get(deletedRemarkN-1).getId();
        assertThat(remarkRepository.findById(deletedRemarkId)).isPresent();
        var remark = remarkRepository.findById(deletedRemarkId);
        remarkRepository.deleteById(deletedRemarkId);
        Throwable exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            remarkRepository.findById(deletedRemarkId).orElseThrow(() -> new EntityNotFoundException("Remark with id %s not found".formatted(deletedRemarkId)));});
        assertEquals("Remark with id %s not found".formatted(deletedRemarkId), exception.getMessage());
    }

    private List<Remark> convertToFlatDbRemarks() {
        return remarks.stream().flatMap(x->x.stream()).collect(Collectors.toList());
    }

    private List<List<Remark>> getDbRemarks() {
        return IntStream.range(1, 4).boxed()
                .map(id -> IntStream.range(1, id+1).boxed()
                        .map(id1 ->{return new Remark(allEntitiesModelImpl.getRemarks().get(id*(id-1)/2+id1-1).getId(), "Remark_"+id+id1, allEntitiesModelImpl.getBooks().get(id-1).getId());}).toList())
                .toList();
    }

}