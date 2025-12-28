package servicios;

import domain.Archivos.Directorio;
import domain.Exceptions.ResourceNotFoundException;
import domain.Juegos.Juego;
import repositorios.JuegoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class JuegosService {
    private final JuegoRepository juegoRepository;
    public JuegosService(JuegoRepository juegoRepository) {
        this.juegoRepository = juegoRepository;
    }
    public Directorio obtenerDirectorioPorJuegoEId(String idJuego, String idDirectorio) throws NoSuchElementException, ResourceNotFoundException {
        Optional<Juego> juego = juegoRepository.findById(idJuego);
        if(juego.isEmpty()){
            throw new ResourceNotFoundException("No se encontro el directorio");
        }
        return juego.get().getSaveFilePaths().stream().filter(dir -> Objects.equals(dir.getId(), idDirectorio)).findFirst().orElseThrow();
    }

    public Juego obtenerJuegoPorId(String uuid) throws ResourceNotFoundException {
        Optional<Juego> juego =  juegoRepository.findById(uuid);
        if(juego.isPresent()) {
            return juego.get();
        }
        else{
            throw new ResourceNotFoundException("Juego no encontrado");
        }
    }
    //TODO: Implementar paginacion
    public List<Juego> getJuegos () throws ResourceNotFoundException {
        return juegoRepository.findAll();
    }

    public void guardarJuego(Juego juego){
        juegoRepository.save(juego);
    }

    public void eliminarJuego(String juegoId) throws ResourceNotFoundException{
        obtenerJuegoPorId(juegoId);
        juegoRepository.deleteById(juegoId);
    }
    public void eliminarJuego(Juego juego) throws ResourceNotFoundException{
        juegoRepository.deleteById(juego.getId());
    }

}

