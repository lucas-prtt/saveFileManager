package repositorios;

import domain.Archivos.checkpoint.ArchivoFinal;
import domain.Archivos.checkpoint.Binario;
import domain.Archivos.checkpoint.GrupoDeDatos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import utils.EntityManagerProvider;
import utils.Tx;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ArchivoRepository {

    public ArchivoRepository() {}
    private EntityManager em(){
        return EntityManagerProvider.get();
    }
    public Binario save(Binario binario){
        return em().merge(binario);
    }
    public Optional<Binario> findByHash(String hash){
        return Tx.run(
                ()->{
                    return Optional.ofNullable(em().find(Binario.class, hash));
                }
        );
    }
    public void remove(Binario binario){
        Tx.runVoid(()->{
            Binario binarioBD = em().merge(binario);
            em().remove(binarioBD);});
    }
    public List <Binario> obtenerHuerfanos(){
        return Tx.run(()->{
            return em().createQuery("SELECT b FROM Binario b WHERE usos = 0", Binario.class).getResultList();
        });
    }

    public GrupoDeDatos merge(GrupoDeDatos grupoDeDatos) {
        return em().merge(grupoDeDatos);
    }

    public Binario merge(Binario binario) {
        return em().merge(binario);
    }
}
