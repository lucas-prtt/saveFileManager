package servicios;

import domain.Juegos.Partida;
import repositorios.PartidaRepository;
import org.springframework.stereotype.Service;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final JuegosService juegosService;

    public PartidaService(PartidaRepository partidaRepository, JuegosService juegosService) {
        this.partidaRepository = partidaRepository;
        this.juegosService = juegosService;
    }
    public void guardarPartida(Partida partida){
        partidaRepository.save(partida);
    }
    public void eliminarPartida(Partida partida){
        partidaRepository.deleteById(partida.getId());
    }
}
