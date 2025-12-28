package servicios;

import domain.Juegos.Checkpoint;
import domain.Juegos.Juego;
import domain.Juegos.Partida;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import repositorios.PartidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;

    public PartidaService(PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }
    @Transactional
    public void guardarPartida(Partida partida){
        partidaRepository.save(partida);
    }
    public void eliminarPartida(Partida partida){
        partidaRepository.deleteById(partida.getId());
    }
    @Transactional
    public List<Checkpoint> obtenerCheckpoints(Partida partida) {
        List<Checkpoint> checkpoints = partidaRepository.findById(partida.getId()).orElseThrow().getCheckpoints();
        Hibernate.initialize(checkpoints);
        return checkpoints;
    }
}
