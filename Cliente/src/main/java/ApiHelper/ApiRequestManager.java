package ApiHelper;

import ApiClients.CheckpointClient;
import ApiClients.JuegoClient;
import ApiClients.PartidaClient;
import Archivos.Archivo;
import JuegosDtos.CheckpointDTO;
import JuegosDtos.JuegoDTO;
import JuegosDtos.PartidaDTO;
import ServerManagment.ServerConnection;

import java.util.List;

public class ApiRequestManager {
    ServerConnection connection;

    public ApiRequestManager(ServerConnection serverConnection){
        connection = serverConnection;
    }

    // JUEGOS

    // GET JUEGOS
    public List<String> obtenerTitulosJuegos(){
        return JuegoClient.obtenerTitulosJuegos(connection);
    }
    public JuegoDTO obtenerJuego(String titulo){
        return JuegoClient.obtenerJuego(connection, titulo);
    }

    // POST JUEGOS
    public JuegoDTO postearJuego(JuegoDTO juego) {
        return JuegoClient.postearJuego(connection, juego);
    }

    // DELETE JUEGOS
    public void eliminarJuego(String tituloJuego) {
        JuegoClient.eliminarJuego(connection, tituloJuego);
    }

    // PATCH JUEGOS
    public JuegoDTO patchearJuego(String tituloJuego, JuegoDTO patchDTO) {
        return JuegoClient.patchearJuego(connection, tituloJuego, patchDTO);
    }


    // PARTIDAS

    // GET PARTIDAS
    public List<String> obtenerTitulosPartidas(String titulo){
        return PartidaClient.obtenerTitulosPartidas(connection, titulo);
    }
    public PartidaDTO obtenerPartida( String tituloJuego, String tituloPartida){
        return PartidaClient.obtenerPartida(connection, tituloJuego, tituloPartida);
    }
    public String obtenerPartidaActual(String tituloJuego){
        return PartidaClient.obtenerPartidaActual(connection, tituloJuego);
    }

    // POST PARTIDAS
    public PartidaDTO postearPartida(String tituloJuego, PartidaDTO partida) {
        return  PartidaClient.postearPartida(connection, tituloJuego, partida);
    }

    // DELETE PARTIDAS
    public void eliminarPartida(String tituloJuego, String tituloPartida) {
        PartidaClient.eliminarPartida(connection, tituloJuego, tituloPartida);
    }

    // PATCH PARTIDAS
    public PartidaDTO patchearPartida(String tituloJuego, String tituloPartida, PartidaDTO patchDTO) {
        return PartidaClient.patchearPartida(connection, tituloJuego, tituloPartida, patchDTO);
    }

    // CHECKPOINTS

    // GET CHECKPOINT
    public List<CheckpointDTO> obtenerCheckpointsDTO(String tituloJuego, String tituloPartida){
        return CheckpointClient.obtenerCheckpointsDTO(connection, tituloJuego, tituloPartida);
    }
    public CheckpointDTO obtenerCheckpoint(String tituloJuego, String tituloPartida, String uuidCheckpoint){
        return CheckpointClient.obtenerCheckpoint(connection, tituloJuego, tituloPartida, uuidCheckpoint);
    }
    public List<Archivo> obtenerArchivosCheckpoint(String tituloJuego, String tituloPartida, String uuidCheckpoint){
        return CheckpointClient.obtenerArchivosCheckpoint(connection, tituloJuego, tituloPartida, uuidCheckpoint);
    }

    // POST CHECKPOINT
    public CheckpointDTO postearCheckpoint(String tituloJuego, String tituloPartida, CheckpointDTO checkpoint) {
        return CheckpointClient.postearCheckpoint(connection, tituloJuego, tituloPartida, checkpoint);
    }
    public CheckpointDTO postearArchivos(String tituloJuego, String tituloPartida,String uuidCheckpoint , List<Archivo> archivos) {
        return CheckpointClient.postearArchivos(connection, tituloJuego, tituloPartida, uuidCheckpoint, archivos);
    }

    // DELETE CHECKPOINT
    public void eliminarCheckpoint(String tituloJuego, String tituloPartida, String uuidCheckpoint) {
        CheckpointClient.eliminarCheckpoint(connection, tituloJuego, tituloPartida, uuidCheckpoint);
    }

}
