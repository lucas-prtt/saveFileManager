package utils;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.OptimisticLockException;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Tx {

    private Tx() {}

    public static void runVoid(Runnable runnable) {
        runVoid(em -> {runnable.run();});
    }

    public static <T> T run(Function<EntityManager, T> work) {
        EntityManager em = EntityManagerProvider.get();
        EntityTransaction tx = em.getTransaction();
        boolean inicioTransaccion = false;
        try {
            if (!tx.isActive()) {
                tx.begin();
                inicioTransaccion = true;
            }
            T result = work.apply(em);
            if(inicioTransaccion)
                tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (inicioTransaccion && tx.isActive())
                tx.rollback();
            throw e;
        }
    }
    public static <T> T run(Supplier<T> supplier) {
        return run((em) -> supplier.get());
    }
    public static void runVoid(Consumer<EntityManager> work) {
        run(em -> {
            work.accept(em);
            return null;
        });
    }

    public static void optimisticLockTry(Integer veces, Runnable runnable){
        Integer intentos = 0;
        do {
            try {
                runnable.run();
            }catch (OptimisticLockException optimisticLockException){
                System.out.println("Optimistic lock exception atrapada. Reintentando (" + veces + ")");
                intentos++;
            }
        }while (intentos<=veces);
    }
}