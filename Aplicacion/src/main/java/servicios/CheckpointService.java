package servicios;

import domain.Archivos.checkpoint.GrupoDeDatos;
import domain.Exceptions.ResourceAlreadyExistsException;
import domain.Exceptions.ResourceNotFoundException;
import domain.Juegos.Checkpoint;
import domain.Juegos.Partida;
import org.springframework.transaction.annotation.Transactional;
import repositorios.CheckpointRepository;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
public class CheckpointService {

    private final CheckpointRepository checkpointRepository;
    private final JuegosService juegosService;
    private final PartidaService partidaService;
    private final FileManager fileManager;

    public CheckpointService(CheckpointRepository checkpointRepository, JuegosService juegosService, PartidaService partidaService, FileManager fileManager) {
        this.checkpointRepository = checkpointRepository;
        this.juegosService = juegosService;
        this.partidaService = partidaService;
        this.fileManager = fileManager;
    }
    @Transactional
    public void guardarCheckpoint(Partida partida, String nombre, String descripcion) throws ResourceNotFoundException, ResourceAlreadyExistsException{
        Partida partidaBD = partidaService.obtenerPartida(partida.getId()).orElseThrow();
        Checkpoint checkpoint = partidaBD.crearCheckpoint(nombre);
        checkpoint.setDescripcion(descripcion);
        partidaBD.getJuego().setPartidaActual(partidaBD);
        List<GrupoDeDatos> nuevosDatos = fileManager.guardarArchivos(partidaBD.getJuego());
        checkpoint.setArchivos(nuevosDatos);
    }
    @Transactional
    public void eliminarCheckpoint(Checkpoint checkpoint) throws ResourceNotFoundException{
        Partida partidaBd = partidaService.obtenerPartida(checkpointRepository.findById(checkpoint.getId()).orElseThrow().getPartida().getId()).orElseThrow();
        partidaBd.eliminarCheckpointById(checkpoint.getId());
        partidaService.guardarPartida(partidaBd);
    }
    @Transactional
    public void cargarCheckpoint(Checkpoint checkpoint) {
        Checkpoint checkpointBd = checkpointRepository.findById(checkpoint.getId()).orElseThrow();
        Partida partidaBd = partidaService.obtenerPartida(checkpointBd.getPartida().getId()).orElseThrow();
        partidaBd.cargarCheckpoint(checkpointBd);
        System.out.println("Se cargo el checkpoint "+checkpoint.getId() + " - " + checkpoint.getDescripcion());
        Map<GrupoDeDatos, Path> archivosACargar = fileManager.hallarPaths(checkpointBd.getArchivos());
        fileManager.cargarArchivos(archivosACargar);
        partidaBd.getJuego().setPartidaActual(partidaBd);
        System.out.println();
    }
    @Transactional
    public void guardarCheckpointUltimaPartida(Partida partida){
        Partida partidaNuevaBd = partidaService.obtenerPartida(partida.getId()).orElseThrow();
        Partida partidaRecienteBd = partidaNuevaBd.getJuego().getPartidaActual();
        guardarCheckpoint(partidaRecienteBd, "Checkpoint de seguridad", "Se guardo este checkpoint automaticamente porque se paso a la partida '" + partidaNuevaBd.getTitulo() + "'");
    }

}
