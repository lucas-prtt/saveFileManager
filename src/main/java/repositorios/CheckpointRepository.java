package repositorios;

import domain.Juegos.Checkpoint;
import jakarta.persistence.EntityManager;
import utils.EntityManagerProvider;
import utils.Tx;

import java.util.Optional;

public class CheckpointRepository {


    public CheckpointRepository() {}

    public Optional<Checkpoint> findById(String id) {
        return Optional.ofNullable(em().find(Checkpoint.class, id));
    }
    private EntityManager em(){
        return EntityManagerProvider.get();
    }
    public void save(Checkpoint checkpoint) {
        Tx.runVoid(()->{
        if (checkpoint.getId() == null) {
            em().persist(checkpoint);
        } else {
            em().merge(checkpoint);
        }
        });
    }

    public void deleteById(String id) {
        Tx.runVoid(()->{
        Checkpoint c = em().find(Checkpoint.class, id);
        if (c != null) {
            em().remove(c);
        }
        });
    }
}

