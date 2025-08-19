package Servicios;

import Exceptions.ResourceAlreadyExistsException;
import Exceptions.ResourceNotFoundException;
import Juegos.Juego;
import Juegos.JuegoPatchDTO;
import Repositorios.JuegoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.nio.file.Files.delete;

@Service
public class JuegosService {
    private final JuegoRepository juegoRepository;
    public JuegosService(JuegoRepository juegoRepository) {
        this.juegoRepository = juegoRepository;
    }

    public List<String> titulosDeTodosLosJuegos(){
        return juegoRepository.findAll().stream().map(Juego::getTitulo).toList();
    }
    public Juego obtenerJuegoPorTitulo(String titulo) throws ResourceNotFoundException {
        Optional<Juego> juego =  juegoRepository.findById(titulo);
        if(juego.isPresent())
            return juego.get();
        else
            throw new ResourceNotFoundException("Juego no encontrado");
    }
    public void guardarNuevoJuego(Juego juego) throws ResourceAlreadyExistsException{
        if(juegoRepository.findById(juego.getTitulo()).isPresent())
            throw new ResourceAlreadyExistsException("Juego ya existe con el mismo titulo");
        juegoRepository.save(juego);
    }

    public void actualizarJuego(Juego juego){
        juegoRepository.save(juego);
    }

    public void eliminarJuego(String juego) throws ResourceNotFoundException{
        obtenerJuegoPorTitulo(juego);
        juegoRepository.deleteById(juego);
    }

    public void patchJuegoWithDTO(String juegoTitulo, JuegoPatchDTO patch) throws ResourceNotFoundException{
        Juego juego = obtenerJuegoPorTitulo(juegoTitulo);
        juego.patchWithDto(patch);
        actualizarJuego(juego);
    }

}

