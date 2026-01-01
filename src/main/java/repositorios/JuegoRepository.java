package repositorios;

import domain.Juegos.Juego;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.EntityManagerProvider;
import utils.JpaUtil;
import utils.Tx;

import java.util.List;
import java.util.Optional;

public class JuegoRepository {


    public JuegoRepository() {}
    private EntityManager em(){
        return EntityManagerProvider.get();
    }

    public Optional<Juego> findById(String id) {
            return Optional.ofNullable(em().find(Juego.class, id));
    }

    public List<Juego> findAll() {
            return em().createQuery("FROM Juego", Juego.class).getResultList();
    }

    public void save(Juego juego) {
        Tx.runVoid(() -> {
                try {
                    em().merge(juego);
                } catch (Exception e) {
                    throw e;
                }
            }
        );
    }

    public void deleteById(String id) {
        Tx.runVoid(() -> {
            try {
                Juego juego = em().find(Juego.class, id);
                if (juego != null) {
                    em().remove(juego);
                }
            }catch (Exception e){
                throw e;
            }
        });
    }
}
