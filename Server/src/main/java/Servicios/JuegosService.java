package Servicios;

import Juegos.Juego;
import Repositorios.CheckpointRepository;
import Repositorios.JuegoRepository;
import Repositorios.PartidaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JuegosService {
    private final JuegoRepository juegoRepository;
    public JuegosService(JuegoRepository juegoRepository) {
        this.juegoRepository = juegoRepository;
    }

    public List<String> titulosDeTodosLosJuegos(){
        return juegoRepository.findAll().stream().map(Juego::getTitulo).toList();
    }
    public Juego obtenerJuegoPorTitulo(String titulo) throws Exception {
        Optional<Juego> juego =  juegoRepository.findById(titulo);
        if(juego.isPresent())
            return juego.get();
        else
            throw new Exception("Juego no encontrado");
    }

}

