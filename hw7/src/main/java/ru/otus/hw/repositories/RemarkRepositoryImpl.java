package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Remark;

import java.util.Optional;

@Component
public class RemarkRepositoryImpl /*implements RemarkRepository*/{

    @PersistenceContext
    private final EntityManager em;

    public RemarkRepositoryImpl(EntityManager em) {
        this.em = em;
    }

//    @Override
    public Optional<Remark> findById(long id) {
        return Optional.ofNullable(em.find(Remark.class, id));
    }

//    @Override
    public Remark save(Remark remark) {
        if (remark.getId() == 0)
            em.persist(remark);
        else
            em.merge(remark);
        return remark;
    }

//    @Override
    public void deleteById(long id) {
        Remark remark = em.find(Remark.class, id);
        if (remark != null)
            em.remove(remark);
    }
}
