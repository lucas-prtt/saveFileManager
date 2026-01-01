package servicios;

import domain.Juegos.Checkpoint;
import domain.Juegos.Juego;
import domain.Juegos.Partida;
import org.hibernate.Hibernate;
import repositorios.PartidaRepository;
import utils.Tx;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PartidaService {

    private final PartidaRepository partidaRepository;

    private JuegosService juegosService;
    public PartidaService(PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }
    public void guardarPartida(Partida partida){
        partidaRepository.save(partida);
    }
    public void eliminarPartida(Partida partida){
        partidaRepository.deleteById(partida.getId());
    }
    public List<Checkpoint> obtenerCheckpoints(Partida partida) {
        return Tx.run( () ->
                {
                    List<Checkpoint> checkpoints = partidaRepository.findById(partida.getId()).orElseThrow().getCheckpoints();
                    Hibernate.initialize(checkpoints);
                    return checkpoints;
                }
        );
    }
    public Optional<Partida> obtenerPartida(String partidaId){
        return partidaRepository.findById(partidaId);
    }
    public boolean isPartidaActualCargada(Partida partida){
        return Tx.run( () ->
                {
                    Partida partidaDb = partidaRepository.findById(partida.getId()).orElseThrow();
                    Juego juego = partidaDb.getJuego();
                    return juego.getPartidaActual()==null || Objects.equals(juego.getPartidaActual().getId(), partida.getId());
                }
        );
    }
}
