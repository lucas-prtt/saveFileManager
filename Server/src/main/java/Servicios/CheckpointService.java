package Servicios;

import Juegos.Checkpoint;
import Juegos.Partida;
import Repositorios.CheckpointRepository;
import Repositorios.JuegoRepository;
import Repositorios.PartidaRepository;

import java.util.Optional;

public class CheckpointService {

    private final CheckpointRepository checkpointRepository;
    private final JuegosService juegosService;
    private final PartidaService partidaService;

    public CheckpointService(CheckpointRepository checkpointRepository, JuegosService juegosService, PartidaService partidaService) {
        this.checkpointRepository = checkpointRepository;
        this.juegosService = juegosService;
        this.partidaService = partidaService;
    }

    public Checkpoint obtenerCheckpointPorJuegoPartidaYUuid(String juegotitulo, String partidatitulo, String uuid) throws Exception {
        Partida partida = partidaService.obtenerPartidaDeJuegoPorTitulo(juegotitulo, partidatitulo);
        Optional<Checkpoint> c = partida.getCheckpointById(uuid);
        if(c.isPresent()){
            return c.get();
        }
        else
            throw new Exception("No se encontro la partida");
    }
}
