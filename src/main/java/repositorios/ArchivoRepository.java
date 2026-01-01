package repositorios;

import domain.Archivos.checkpoint.ArchivoFinal;
import domain.Archivos.checkpoint.GrupoDeDatos;
import jakarta.persistence.EntityManager;
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
    public Optional<ArchivoFinal> findByHash(String hash){
        return Tx.run(() -> {
            var resultados = em().createQuery("SELECT a FROM ArchivoFinal a WHERE a.hash = :hash", ArchivoFinal.class)
                    .setParameter("hash", hash)
                    .getResultList();
            return resultados.isEmpty() ? Optional.empty(): Optional.of(resultados.getFirst());
        });
    }
    public Set<String> obtenerHashArchivosUsados() {
        return Tx.run(() -> {
            // Archivos finales existentes
            List<ArchivoFinal> todosLosArchivos = em().createQuery(
                    "SELECT a FROM ArchivoFinal a", ArchivoFinal.class
            ).getResultList();
            System.out.println(todosLosArchivos.size() + " - Todos los archivos");

            return todosLosArchivos.stream().map(ArchivoFinal::getHash).collect(Collectors.toSet());
        });
    }

}
