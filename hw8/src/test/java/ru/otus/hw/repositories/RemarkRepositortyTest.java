package ru.otus.hw.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Remark;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.hw.repositories"})
@DisplayName("Mongo репозиторий для Remark")
class RemarkRepositortyTest {

    @Autowired
    private RemarkRepository remarkRepository;

    private final String updatedRemarkId = "65bb622bd4e0735ea8c52fe2";

    private final String deletedRemarkId = "65bb622bd4e0735ea8c52fe3";

    @BeforeEach
    void setUp() {
    }
//
//    @DisplayName("Замечание по id")
//    @ParameterizedTest
//    @MethodSource("convertToFlatDbRemarks")
//    void findById(Remark remark) {
//        if (remark.getId() != updatedRemarkId && remark.getId() != deletedRemarkId) {
//            var fRemark = remarkRepository.findById(remark.getId()).orElseThrow(() -> new EntityNotFoundException("Remark not found, id %d".formatted(remark.getId())));
//            assertThat(fRemark.getId()).isEqualTo(remark.getId());
//            assertThat(fRemark.getRemarkText()).isEqualTo(remark.getRemarkText());
//            assertThat(fRemark.getBook().getId()).isEqualTo(remark.getBook().getId());
//        }
//    }
//
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

    @DisplayName("Удаление замечания")
    @Test
    void deleteById() {
        assertThat(remarkRepository.findById(deletedRemarkId)).isPresent();
        var remark = remarkRepository.findById(deletedRemarkId);
        remarkRepository.deleteById(deletedRemarkId);
        Throwable exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            remarkRepository.findById(deletedRemarkId).orElseThrow(() -> new EntityNotFoundException("Remark with id %d not found".formatted(deletedRemarkId)));});
        assertEquals("findById error!", exception.getMessage(), "Remark with id %s not found".formatted(deletedRemarkId));
    }

    private static List<Remark> convertToFlatDbRemarks() {
        return getDbRemarks().stream().flatMap(x->x.stream()).collect(Collectors.toList());
    }

    private static List<List<Remark>> getDbRemarks() {
        return IntStream.range(1, 4).boxed()
                .map(id -> IntStream.range(1, id+1).boxed()
                        .map(id1 ->{ Book book = new Book( id.toString(), null, null, null, null);
                            return new Remark(id1.toString(), "Remark_"+id+id1, id.toString());}).toList())
                .toList();
    }

}