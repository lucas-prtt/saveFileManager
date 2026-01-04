package utils;

import jakarta.persistence.EntityManager;

public class EntityManagerProvider {
    private static ThreadLocal<EntityManager> entityManagerThreadLocal = new ThreadLocal<>();
    public static EntityManager get(){
        EntityManager em = entityManagerThreadLocal.get();
        if(em == null){
            entityManagerThreadLocal.set(JpaUtil.em());
            em = entityManagerThreadLocal.get();
        }
        return em;
    }
    public static void clear(){
        EntityManager em = entityManagerThreadLocal.get();
        if(em == null)
            return;
        if(em.isOpen())
            em.close();
        entityManagerThreadLocal.remove();
    }
}
