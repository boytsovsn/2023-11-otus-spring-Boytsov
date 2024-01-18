package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Remark;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaRemarkRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@DisplayName("JPA сервис для Remark")
@DataJpaTest
@Import({RemarkServiceImpl.class, JpaRemarkRepository.class, JpaBookRepository.class})
@Transactional(propagation = Propagation.NEVER)
class RemarkServiceImplTest {

    private List<List<Remark>> dbRemarks;

    private List<Remark> dbFlatRemarks;
    @Autowired
    private RemarkService remarkServiceImpl;

    @BeforeEach
    void setUp() {
        dbRemarks = getDbRemarks();
        setDbFlatRemarks(convertToFlatDbRemarks(dbRemarks));
    }

    @DisplayName("Список замечений для книги")
    @Test
    void findByBookId() {
        List<Remark> remark;
        for (int i = 1; i < dbRemarks.size(); i++) {
            List<Remark> remarks = remarkServiceImpl.findByBookId(dbRemarks.get(i).get(0).getBookId());
            assertThat(remarks).containsExactlyElementsOf(dbRemarks.get(i));
        }
    }

    @DisplayName("Замечание по id")
    @ParameterizedTest
    @MethodSource("getDbFlatRemarks")
    void findById(Remark remark) {
        var fRemark = remarkServiceImpl.findById(remark.getId()).orElseThrow(()->new EntityNotFoundException("Remark not found, id %d".formatted(remark.getId())));
        assertThat(fRemark).isEqualTo(remark);
    }

    @DisplayName("Вставка замечания")
    @Test
    void insertRemark() {
        long bookId = 2L;
        var newRemark = new Remark(0, "Remark_10500", bookId);
        var returnedRemark = remarkServiceImpl.save(0, "Remark_10500", bookId);
        newRemark.setId(returnedRemark.getId());
        assertThat(returnedRemark).isNotNull()
                .matches(remark -> remark.getId() > 0)
                .isEqualTo(newRemark);
        Optional<Remark> checkRemark = remarkServiceImpl.findById(returnedRemark.getId());
        assertThat(checkRemark)
                .isPresent()
                .get()
                .isEqualTo(returnedRemark);
    }

    @DisplayName("Обновление замечания")
    @Test
    void updateRemark() {
        long updatedRemarkId = 1L;
        long forBookId = 2L;
        var expectedRemark = new Remark(updatedRemarkId, "Remark_10500", forBookId);

        assertThat(remarkServiceImpl.findById(expectedRemark.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedRemark);

        var returnedRemark = remarkServiceImpl.save(updatedRemarkId, "Remark_10500", forBookId);
        assertThat(returnedRemark).isNotNull()
                .matches(remark -> remark.getId() > 0)
                .isEqualTo(expectedRemark);

        assertThat(remarkServiceImpl.findById(returnedRemark.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedRemark);
    }

    @DisplayName("Удаление замечания")
    @Test
    void deleteById() {
        long deletedRemarkId = 3L;
        assertThat(remarkServiceImpl.findById(deletedRemarkId)).isPresent();
        remarkServiceImpl.deleteById(deletedRemarkId);
        Throwable exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            remarkServiceImpl.findById(deletedRemarkId).orElseThrow(() -> new EntityNotFoundException("Remark with id %d not found".formatted(deletedRemarkId)));});
        assertEquals("findById error!", exception.getMessage(), "Remark with id %s not found".formatted(deletedRemarkId));
    }

    private static List<Remark> convertToFlatDbRemarks(List<List<Remark>> dbRemarks) {
        return dbRemarks.stream().flatMap(x->x.stream()).collect(Collectors.toList());
    }

    private static List<List<Remark>> getDbRemarks() {
        return IntStream.range(1, 4).boxed()
                .map(id -> IntStream.range(1, id+1).boxed()
                        .map(id1 ->new Remark(id*(id-1)/2+id1,"Remark_"+id+id1, id)).toList())
                .toList();
    }

    static List<Remark> getDbFlatRemarks() {
        return dbFlatRemarks;
    }

    public void setDbFlatRemarks(List<Remark> dbFlatRemarks) {
        this.dbFlatRemarks = dbFlatRemarks;
    }
}