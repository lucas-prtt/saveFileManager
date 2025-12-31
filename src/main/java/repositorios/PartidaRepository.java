package repositorios;
import domain.Juegos.Partida;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class PartidaRepository {

    private final EntityManager em;

    public PartidaRepository(EntityManager em) {
        this.em = em;
    }

    public Optional<Partida> findById(String id) {
        return Optional.ofNullable(em.find(Partida.class, id));
    }
    public void save(Partida partida) {
        if (partida.getId() == null) {
            em.persist(partida);
        } else {
            em.merge(partida);
        }
    }
    public void deleteById(String id) {
        Partida p = em.find(Partida.class, id);
        if (p != null) {
            em.remove(p);
        }
    }
}
