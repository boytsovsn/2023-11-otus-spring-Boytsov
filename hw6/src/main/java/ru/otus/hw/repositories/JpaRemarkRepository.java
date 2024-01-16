package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Remark;

import java.util.List;
import java.util.Optional;

@Repository

public class JpaRemarkRepository implements RemarkRepository{

    @PersistenceContext
    private final EntityManager em;

    public JpaRemarkRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Remark> findByBookId(long id) {
        var query = em.createQuery("select rems from Book r left join fetch r.remarks rems where r.id = :id", Remark.class);
        query.setParameter("id", id);
        return query.getResultList();
    }


    @Override
    public Optional<Remark> findById(long id) {
        return Optional.ofNullable(em.find(Remark.class, id));
    }

    @Override
    public Remark save(Remark remark) {
        if (remark.getId() == 0)
            em.persist(remark);
        else
            em.merge(remark);
        return remark;
    }

    @Override
    public void deleteById(long id) {
        Remark remark = em.find(Remark.class, id);
        if (remark != null)
            em.remove(remark);
    }
}
