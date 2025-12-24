package servicios;

import domain.Archivos.Archivo;
import domain.Exceptions.MalformedResourceException;
import domain.Exceptions.ResourceAlreadyExistsException;
import domain.Exceptions.ResourceNotFoundException;
import domain.Juegos.Checkpoint;
import domain.Juegos.Partida;
import dto.JuegosConverter.CheckpointConverter;
import dto.CheckpointDTO;
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
    private final CheckpointConverter checkpointConverter;

    public CheckpointService(CheckpointRepository checkpointRepository, JuegosService juegosService, PartidaService partidaService, CheckpointConverter checkpointConverter) {
        this.checkpointRepository = checkpointRepository;
        this.juegosService = juegosService;
        this.partidaService = partidaService;
        this.checkpointConverter = checkpointConverter;
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
    public void guardarNuevoCheckpoint(String juegoTitulo, String partidaTitulo, CheckpointDTO checkpointDto) throws ResourceNotFoundException, ResourceAlreadyExistsException{
        Partida partida = partidaService.obtenerPartidaDeJuegoPorTitulo(juegoTitulo, partidaTitulo);
        if(checkpointDto.getId() == null)
            checkpointDto.generateNewId();
        else if(partida.getAllCheckpointsId().contains(checkpointDto.getId()))
            throw new ResourceAlreadyExistsException("El checkpoint con el mismo ID ya existe");
        partida.agregarCheckpoint(checkpointConverter.fromDto(checkpointDto));
        partidaService.actualizarPartida(partida);
    }
    public void eliminarCheckpoint(String juegoTitulo, String partidaTitulo, String uuidCheckpoint) throws ResourceNotFoundException{
        Partida partida = partidaService.obtenerPartidaDeJuegoPorTitulo(juegoTitulo, partidaTitulo);
        if (!partida.getAllCheckpointsId().contains(uuidCheckpoint))
            throw new ResourceNotFoundException("No se encontro el checkpoint");
        partida.eliminarCheckpointById(uuidCheckpoint);
        partidaService.actualizarPartida(partida);
    }

    public void postearArchivos(String juegoTitulo, String partidaTitulo, String uuidCheckpoint, List<Archivo> archivos) throws ResourceNotFoundException, ResourceAlreadyExistsException, NoSuchElementException{
        Checkpoint checkpoint = obtenerCheckpointPorJuegoPartidaYUuid(juegoTitulo, partidaTitulo, uuidCheckpoint);
        if (checkpoint.getArchivos() == null || checkpoint.getArchivos().isEmpty()){
            if (checkpoint.getArchivos() == null){
                checkpoint.setArchivos(new ArrayList<>());
            }
            archivos.forEach(archivo -> archivo.setUbicacion(juegosService.obtenerDirectorioPorJuegoEId(juegoTitulo, archivo.getUbicacion().getId())));
            checkpoint.getArchivos().addAll(archivos);
            checkpointRepository.save(checkpoint);
            return;
        }
        throw new ResourceAlreadyExistsException("Los archivos ya estan definidos");
    }



}
