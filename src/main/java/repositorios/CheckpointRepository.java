package repositorios;

import domain.Juegos.Checkpoint;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class CheckpointRepository {

    private final EntityManager em;

    public CheckpointRepository(EntityManager em) {
        this.em = em;
    }

    public Optional<Checkpoint> findById(String id) {
        return Optional.ofNullable(em.find(Checkpoint.class, id));
    }

    public void save(Checkpoint checkpoint) {
        if (checkpoint.getId() == null) {
            em.persist(checkpoint);
        } else {
            em.merge(checkpoint);
        }
    }

    public void deleteById(String id) {
        Checkpoint c = em.find(Checkpoint.class, id);
        if (c != null) {
            em.remove(c);
        }
    }
}

