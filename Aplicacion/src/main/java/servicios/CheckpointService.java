package servicios;

import domain.Archivos.Archivo;
import domain.Exceptions.MalformedResourceException;
import domain.Exceptions.ResourceAlreadyExistsException;
import domain.Exceptions.ResourceNotFoundException;
import domain.Juegos.Checkpoint;
import domain.Juegos.Partida;
import repositorios.CheckpointRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@Service
public class CheckpointService {

    private final CheckpointRepository checkpointRepository;
    private final JuegosService juegosService;
    private final PartidaService partidaService;

    public CheckpointService(CheckpointRepository checkpointRepository, JuegosService juegosService, PartidaService partidaService) {
        this.checkpointRepository = checkpointRepository;
        this.juegosService = juegosService;
        this.partidaService = partidaService;
    }

    public void guardarCheckpoint(Checkpoint checkpoint) throws ResourceNotFoundException, ResourceAlreadyExistsException{
        checkpointRepository.save(checkpoint);
    }
    public void eliminarCheckpoint(Checkpoint checkpoint) throws ResourceNotFoundException{
        checkpointRepository.deleteById(checkpoint.getId());
    }
}
