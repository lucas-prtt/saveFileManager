package utils;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.function.Consumer;
import java.util.function.Function;

public final class Tx {

    private Tx() {}

    public static <T> T run(Function<EntityManager, T> work) {
        EntityManager em = JpaUtil.em();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T result = work.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public static void runVoid(Consumer<EntityManager> work) {
        run(em -> {
            work.accept(em);
            return null;
        });
    }
}