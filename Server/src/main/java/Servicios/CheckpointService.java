package Servicios;

import Exceptions.MalformedResourceException;
import Exceptions.ResourceAlreadyExistsException;
import Exceptions.ResourceNotFoundException;
import Juegos.Checkpoint;
import Juegos.Partida;
import Repositorios.CheckpointRepository;
import Repositorios.JuegoRepository;
import Repositorios.PartidaRepository;
import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import org.springframework.stereotype.Service;

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

    public Checkpoint obtenerCheckpointPorJuegoPartidaYUuid(String juegotitulo, String partidatitulo, String uuid) throws ResourceNotFoundException {
        Partida partida = partidaService.obtenerPartidaDeJuegoPorTitulo(juegotitulo, partidatitulo);
        Optional<Checkpoint> c = partida.getCheckpointById(uuid);
        if(c.isPresent()){
            return c.get();
        }
        else
            throw new ResourceNotFoundException("No se encontro el checkpoint");
    }
    public void guardarNuevoCheckpoint(String juegoTitulo, String partidaTitulo, Checkpoint checkpoint) throws ResourceNotFoundException, ResourceAlreadyExistsException{
        Partida partida = partidaService.obtenerPartidaDeJuegoPorTitulo(juegoTitulo, partidaTitulo);
        if(checkpoint.getId() == null)
            checkpoint.generateNewId();
        else if(partida.getAllCheckpointsId().contains(checkpoint.getId()))
            throw new ResourceAlreadyExistsException("El checkpoint con el mismo ID ya existe");
        partida.agregarCheckpoint(checkpoint);
        partidaService.actualizarPartida(partida);
    }
    public void eliminarCheckpoint(String juegoTitulo, String partidaTitulo, String uuidCheckpoint) throws ResourceNotFoundException{
        Partida partida = partidaService.obtenerPartidaDeJuegoPorTitulo(juegoTitulo, partidaTitulo);
        if (!partida.getAllCheckpointsId().contains(uuidCheckpoint))
            throw new ResourceNotFoundException("No se encontro el checkpoint");
        partida.eliminarCheckpointById(uuidCheckpoint);
        partidaService.actualizarPartida(partida);
    }



}
