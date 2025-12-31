package repositorios;

import domain.Juegos.Juego;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import utils.JpaUtil;

import java.util.List;
import java.util.Optional;

public class JuegoRepository {

    private final EntityManager em;

    public JuegoRepository(EntityManager em) {
        this.em = em;
    }
    public Optional<Juego> findById(String id) {
        try {
            return Optional.ofNullable(em.find(Juego.class, id));
        } finally {
            em.close();
        }
    }

    public List<Juego> findAll() {
        try {
            return em.createQuery("FROM Juego", Juego.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Juego juego) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(juego);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void deleteById(String id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Juego juego = em.find(Juego.class, id);
            if (juego != null) {
                em.remove(juego);
            }
            tx.commit();
        } finally {
            em.close();
        }
    }
}
