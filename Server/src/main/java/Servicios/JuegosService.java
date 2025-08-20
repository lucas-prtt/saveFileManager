package Servicios;

import Exceptions.ResourceAlreadyExistsException;
import Exceptions.ResourceNotFoundException;
import Juegos.Juego;
import Juegos.JuegoPatchDTO;
import JuegosConverter.JuegoConverter;
import JuegosDtos.JuegoDTO;
import Repositorios.JuegoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.nio.file.Files.delete;

@Service
public class JuegosService {
    private final JuegoRepository juegoRepository;
    private final JuegoConverter juegoConverter;
    public JuegosService(JuegoRepository juegoRepository, JuegoConverter juegoConverter) {
        this.juegoRepository = juegoRepository;
        this.juegoConverter = juegoConverter;
    }

    public List<String> titulosDeTodosLosJuegos(){
        return juegoRepository.findAll().stream().map(Juego::getTitulo).toList();
    }
    public Juego obtenerJuegoPorTitulo(String titulo) throws ResourceNotFoundException {
        Optional<Juego> juego =  juegoRepository.findById(titulo);
        if(juego.isPresent()) {
            return juego.get();
        }
        else{
            throw new ResourceNotFoundException("Juego no encontrado");
        }
    }
    public void guardarNuevoJuego(JuegoDTO juegoDTO) throws ResourceAlreadyExistsException{
        if(juegoRepository.findById(juegoDTO.getTitulo()).isPresent())
            throw new ResourceAlreadyExistsException("Juego ya existe con el mismo titulo");
        Juego juego = juegoConverter.fromDto(juegoDTO);
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

