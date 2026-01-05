package servicios;

import domain.Archivos.checkpoint.*;
import domain.Exceptions.ResourceNotFoundException;
import domain.Juegos.Checkpoint;
import domain.Juegos.Partida;
import repositorios.CheckpointRepository;
import utils.Tx;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

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
    public void guardarCheckpoint(Partida partida, String nombre, String descripcion) throws ResourceNotFoundException{
        List<GrupoDeDatos> nuevosDatos = new ArrayList<>();
        nuevosDatos.addAll(archivoService.guardarArchivos(partida.getJuego()));
        // Grupos de datos tiene archivos binarios ya persistidos en BD y FS, no asociados a ningun checkpoint y con usos en 0, salvo que alguno sea repetido
        // Si tira error mientras se escriben los archivos, quedan con uso en 0 y se eliminan luego
        // Dentro maneja transacciones individuales a nivel archivo

        Tx.runVoid( () ->
                {
                    Partida partidaBD = partidaService.obtenerPartida(partida.getId()).orElseThrow();
                    Checkpoint checkpoint = partidaBD.crearCheckpoint(nombre);
                    checkpoint.setDescripcion(descripcion);
                    partidaBD.getJuego().setPartidaActual(partidaBD);
                    nuevosDatos.forEach(g -> {
                        g.getArchivos().forEach(a -> {
                            a.obtenerArchivosRecursivo().stream().map(ArchivoFinal::getBinario).forEach(binario -> {
                                    binario.aumentarUso();
                                    archivoService.merge(binario);
                                }
                            );
                        });
                    });
                    checkpoint.setArchivos(nuevosDatos);
                    // Se crea un checkpoint con los archivos obtenidos. Si falla crear le checkpoint, los usos quedan en 0 y los archivos se eliminan
                }
        );
        Tx.runVoid( () ->
                {
                    Partida partidaBD = partidaService.obtenerPartida(partida.getId()).orElseThrow();
                    List<Checkpoint> checkpointsABorrar = new ArrayList<> (partidaBD.getCheckpointStrategy().checkpointsABorrar(partidaBD.getCheckpoints()));
                    checkpointsABorrar.forEach((c)->{
                        partidaBD.eliminarCheckpointById(c.getId());
                    });
                }
        );

        archivoService.eliminarArchivosHuerfanos(); // Si alguno quedo sin asociar se elimina
    }
    public void eliminarCheckpoint(Checkpoint checkpoint) throws ResourceNotFoundException{
        Tx.optimisticLockTry(3, 200, () -> {
            Tx.runVoid( () ->
                {
                    Partida partidaBd = partidaService.obtenerPartida(checkpoint.getPartida().getId()).orElseThrow();
                    partidaBd.eliminarCheckpointById(checkpoint.getId());
                    partidaService.guardarPartida(partidaBd);
                }
            );
        });
        archivoService.eliminarArchivosHuerfanos();
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

    public void exportarCheckpoint(Path path, Checkpoint seleccionado) {
        try (ZipOutputStream zos = new ZipOutputStream(
                Files.newOutputStream(path))) {

            for (GrupoDeDatos grupo : seleccionado.getArchivos()) {
                for (Archivo archivo : grupo.getArchivos()) {
                    archivoService.exportarArchivoZip(archivo, "", zos);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
