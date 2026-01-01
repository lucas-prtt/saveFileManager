package repositorios;
import domain.Juegos.Partida;
import jakarta.persistence.EntityManager;
import utils.EntityManagerProvider;
import utils.Tx;

import java.util.Optional;

public class PartidaRepository {


    public PartidaRepository() {}
    private EntityManager em(){
        return EntityManagerProvider.get();
    }
    public Optional<Partida> findById(String id) {
        return Optional.ofNullable(em().find(Partida.class, id));
    }
    public void save(Partida partida) {
        Tx.runVoid(()->{
            if (partida.getId() == null) {
                em().persist(partida);
            } else {
                em().merge(partida);
            }
        });
    }
    public void deleteById(String id) {
        Tx.runVoid(()->{
        Partida p = em().find(Partida.class, id);
        if (p != null) {
            em().remove(p);
        }
        });
    }
}
