package servicios;

import domain.Juegos.Checkpoint;
import domain.Juegos.Juego;
import domain.Juegos.Partida;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import repositorios.PartidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    @Lazy
    @Autowired
    private JuegosService juegosService;
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
    public Optional<Partida> obtenerPartida(String partidaId){
        return partidaRepository.findById(partidaId);
    }
    @Transactional
    public boolean isPartidaActualCargada(Partida partida){
        Partida partidaDb = partidaRepository.findById(partida.getId()).orElseThrow();
        Juego juego = partidaDb.getJuego();
        return juego.getPartidaActual()==null || Objects.equals(juego.getPartidaActual().getId(), partida.getId());
    }
}
