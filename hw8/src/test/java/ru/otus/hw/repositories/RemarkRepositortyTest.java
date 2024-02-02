package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.entities.Book;
import ru.otus.hw.models.entities.Remark;
import ru.otus.hw.models.test.AllEntitiesModel;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.changelogs.test", "ru.otus.hw.repositories", "ru.otus.hw.models.test"})
@DisplayName("Mongo репозиторий для Remark")
class RemarkRepositortyTest {

    @Autowired
    private RemarkRepository remarkRepository;

    @Autowired
    private AllEntitiesModel allEntitiesModelImpl;

    private final String updatedRemarkId = "65bb622bd4e0735ea8c52fe2";

    private final String deletedRemarkId = "65bb622bd4e0735ea8c52fe3";
    private List<List<Remark>> remarks;

    @BeforeEach
    void setUp() {
        remarks = getDbRemarks();
    }

    @DisplayName("Замечание по id")
    @Test
    void findById() {
        List<Remark> remarksList = convertToFlatDbRemarks();
        for (Remark remark: remarksList) {
            if (remark.getId() != updatedRemarkId && remark.getId() != deletedRemarkId) {
                var fRemark = remarkRepository.findById(remark.getId()).orElseThrow(() -> new EntityNotFoundException("Remark not found, id %d".formatted(remark.getId())));
                assertThat(fRemark.getId()).isEqualTo(remark.getId());
                assertThat(fRemark.getRemarkText()).isEqualTo(remark.getRemarkText());
                assertThat(fRemark.getBookId()).isEqualTo(remark.getBookId());
            }
        }
    }

//    @DisplayName("Вставка замечания")
//    @Test
//    void insertRemark() {
//        long insertToBookId = 2L;
//        Book insertToBook = new Book(insertToBookId, null, null, null, null);
//        var newRemark = new Remark(0, "Remark_10500", insertToBook);
//        var returnedRemark = remarkRepository.save(newRemark);
//        newRemark.setId(returnedRemark.getId());
//        assertThat(returnedRemark).isNotNull()
//                .matches(remark -> remark.getId() > 0)
//                .isEqualTo(newRemark);
//        Optional<Remark> checkRemark = remarkRepository.findById(returnedRemark.getId());
//        assertThat(checkRemark)
//                .isPresent()
//                .get()
//                .isEqualTo(returnedRemark);
//        remarkRepository.deleteById(returnedRemark.getId());
//    }
//
//    @DisplayName("Обновление замечания")
//    @Test
//    void updateRemark() {
//
//        long forBookId = 2L;
//        Book updateForBook = new Book(forBookId, null, null, null, null);
//        var expectedRemark = new Remark(updatedRemarkId, "Remark_10500", updateForBook);
//
//        assertThat(remarkRepository.findById(expectedRemark.getId()))
//                .isPresent()
//                .get()
//                .isNotEqualTo(expectedRemark);
//
//        var returnedRemark = remarkRepository.save(expectedRemark);
//        assertThat(returnedRemark).isNotNull()
//                .matches(remark -> remark.getId() > 0)
//                .usingRecursiveComparison().ignoringExpectedNullFields()
//                .isEqualTo(expectedRemark);
//
//        Remark bdReamrk = remarkRepository.findById(returnedRemark.getId()).get();
//        assertThat(bdReamrk.getId())
//                .isEqualTo(returnedRemark.getId());
//        assertThat(bdReamrk.getRemarkText())
//                .isEqualTo(returnedRemark.getRemarkText());
//        assertThat(bdReamrk.getBook().getId())
//                .isEqualTo(returnedRemark.getBook().getId());
//    }

//    @DisplayName("Удаление замечания")
//    @Test
//    void deleteById() {
//        assertThat(remarkRepository.findById(deletedRemarkId)).isPresent();
//        var remark = remarkRepository.findById(deletedRemarkId);
//        remarkRepository.deleteById(deletedRemarkId);
//        Throwable exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
//            remarkRepository.findById(deletedRemarkId).orElseThrow(() -> new EntityNotFoundException("Remark with id %d not found".formatted(deletedRemarkId)));});
//        assertEquals("findById error!", exception.getMessage(), "Remark with id %s not found".formatted(deletedRemarkId));
//    }

    private List<Remark> convertToFlatDbRemarks() {
        return remarks.stream().flatMap(x->x.stream()).collect(Collectors.toList());
    }

    private List<List<Remark>> getDbRemarks() {
        return IntStream.range(1, 4).boxed()
                .map(id -> IntStream.range(1, id+1).boxed()
                        .map(id1 ->{ Book book = new Book( allEntitiesModelImpl.getBooks().get(id-1).getId(), null, null, null, null);
                            return new Remark(allEntitiesModelImpl.getRemarks().get(id*(id-1)/2+id1-1).getId(), "Remark_"+id+id1, allEntitiesModelImpl.getBooks().get(id-1).getId());}).toList())
                .toList();
    }

}