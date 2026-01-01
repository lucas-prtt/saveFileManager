package servicios;

import domain.Archivos.checkpoint.Archivo;
import domain.Archivos.checkpoint.ArchivoFinal;
import domain.Archivos.checkpoint.GrupoDeDatos;
import domain.Exceptions.ResourceAlreadyExistsException;
import domain.Exceptions.ResourceNotFoundException;
import domain.Juegos.Checkpoint;
import domain.Juegos.Partida;
import repositorios.CheckpointRepository;
import utils.EntityManagerProvider;
import utils.Tx;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class CheckpointService {

    private final CheckpointRepository checkpointRepository;
    private final JuegosService juegosService;
    private final PartidaService partidaService;
    private final ArchivoService archivoService;

    public CheckpointService(CheckpointRepository checkpointRepository, JuegosService juegosService, PartidaService partidaService, ArchivoService archivoService) {
        this.checkpointRepository = checkpointRepository;
        this.juegosService = juegosService;
        this.partidaService = partidaService;
        this.archivoService = archivoService;
    }
    public void guardarCheckpoint(Partida partida, String nombre, String descripcion) throws ResourceNotFoundException, ResourceAlreadyExistsException{
        Tx.runVoid( () ->
                {
                    Partida partidaBD = partidaService.obtenerPartida(partida.getId()).orElseThrow();
                    Checkpoint checkpoint = partidaBD.crearCheckpoint(nombre);
                    checkpoint.setDescripcion(descripcion);
                    partidaBD.getJuego().setPartidaActual(partidaBD);
                    List<GrupoDeDatos> nuevosDatos = archivoService.guardarArchivos(partidaBD.getJuego());
                    checkpoint.setArchivos(nuevosDatos);
                }
        );
    }
    public void eliminarCheckpoint(Checkpoint checkpoint) throws ResourceNotFoundException{
        Tx.runVoid( () ->
                {
                    Partida partidaBd = partidaService.obtenerPartida(checkpointRepository.findById(checkpoint.getId()).orElseThrow().getPartida().getId()).orElseThrow();
                    partidaBd.eliminarCheckpointById(checkpoint.getId());
                    partidaService.guardarPartida(partidaBd);
                    EntityManagerProvider.get().flush();
                    archivoService.eliminarArchivosHuerfanos();
                }
        );
    }


    public void cargarCheckpoint(Checkpoint checkpoint) {
        Tx.runVoid( () ->
                {
                    Checkpoint checkpointBd = checkpointRepository.findById(checkpoint.getId()).orElseThrow();
                    Partida partidaBd = partidaService.obtenerPartida(checkpointBd.getPartida().getId()).orElseThrow();
                    partidaBd.cargarCheckpoint(checkpointBd);
                    System.out.println("Se cargo el checkpoint "+checkpoint.getId() + " - " + checkpoint.getDescripcion());
                    Map<GrupoDeDatos, Path> archivosACargar = archivoService.hallarPaths(checkpointBd.getArchivos());
                    archivoService.cargarArchivos(archivosACargar);
                    partidaBd.getJuego().setPartidaActual(partidaBd);
                    System.out.println();
                }
        );
    }
    public void guardarCheckpointUltimaPartida(Partida partida){
        Tx.runVoid( () ->
                {
                    Partida partidaNuevaBd = partidaService.obtenerPartida(partida.getId()).orElseThrow();
                    Partida partidaRecienteBd = partidaNuevaBd.getJuego().getPartidaActual();
                    guardarCheckpoint(partidaRecienteBd, "Checkpoint de seguridad", "Se guardo este checkpoint automaticamente porque se paso a la partida '" + partidaNuevaBd.getTitulo() + "'");
                }
        );
    }

}
