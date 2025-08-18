package Servicios;

import Juegos.Checkpoint;
import Juegos.Partida;
import Repositorios.CheckpointRepository;
import Repositorios.JuegoRepository;
import Repositorios.PartidaRepository;

import java.util.Optional;

public class CheckpointService {

    private final CheckpointRepository checkpointRepository;

    public CheckpointService(CheckpointRepository checkpointRepository) {
        this.checkpointRepository = checkpointRepository;
    }

    public Checkpoint obtenerCheckpointPorId(Partida partida, String uuid) throws Exception {
        Optional<Checkpoint> c = partida.getCheckpointById(uuid);
        if(c.isPresent()){
            return c.get();
        }
        else
            throw new Exception("No se encontro la partida");
    }
}
