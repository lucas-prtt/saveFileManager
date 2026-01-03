package servicios;

import domain.Archivos.juego.Directorio;
import domain.Exceptions.ResourceNotFoundException;
import domain.Juegos.Juego;
import domain.Juegos.Partida;
import org.hibernate.Hibernate;
import repositorios.JuegoRepository;
import utils.Tx;

import java.util.*;

public class JuegosService {
    private final JuegoRepository juegoRepository;
    private final PartidaService partidaService;
    private final ArchivoService archivoService;
    public JuegosService(JuegoRepository juegoRepository, PartidaService partidaService, ArchivoService archivoService) {
        this.juegoRepository = juegoRepository;
        this.partidaService = partidaService;
        this.archivoService = archivoService;
    }
    public Directorio obtenerDirectorioPorJuegoEId(String idJuego, String idDirectorio) throws NoSuchElementException, ResourceNotFoundException {
        Optional<Juego> juego = juegoRepository.findById(idJuego);
        if(juego.isEmpty()){
            throw new ResourceNotFoundException("No se encontro el directorio");
        }
        return juego.get().getSaveFilePaths().stream().filter(dir -> Objects.equals(dir.getId(), idDirectorio)).findFirst().orElseThrow();
    }
    public Juego obtenerJuegoPorId(String uuid) throws ResourceNotFoundException {
        return juegoRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Juego no encontrado"));
    }
    //TODO: Implementar paginacion
    public List<Juego> getJuegos () throws ResourceNotFoundException {
        return juegoRepository.findAll();
    }

    public void guardarJuego(Juego juego){
        juegoRepository.save(juego);
    }

    public void eliminarJuego(String juegoId) throws ResourceNotFoundException{
        Tx.runVoid( () ->
                {
                    obtenerJuegoPorId(juegoId);
                    juegoRepository.deleteById(juegoId);
                }
        );
    }
    public void eliminarJuego(Juego juego) throws ResourceNotFoundException{

        Tx.runVoid( () ->
                {
                    juegoRepository.deleteById(juego.getId());
                    archivoService.eliminarArchivosHuerfanos();
                }
        );
    }
    public List<Partida> getPartidas(Juego juego) {
        return Tx.run( () ->
            {
                List<Partida> partidas = juegoRepository.findById(juego.getId()).map(Juego::getPartidas).orElse(new ArrayList<>());
                Hibernate.initialize(partidas);
                return partidas;
            }
        );
    }
    public void agregarPartida(Juego juego, Partida partida) {
        Tx.runVoid( () ->
            {
                Juego juegoBD = obtenerJuegoPorId(juego.getId());
                juegoBD.agregarPartida(partida);
            }
        );
    }

    public void eliminarPartida(Partida selectedItem) {
        Tx.runVoid( () ->
                {
                    Partida partidaBd = partidaService.obtenerPartida(selectedItem.getId()).orElseThrow();
                    Juego juegoBD = obtenerJuegoPorId(selectedItem.getJuego().getId());
                    juegoBD.eliminarPartida(partidaBd);
                    archivoService.eliminarArchivosHuerfanos();
                }
        );
    }

    public void modificarJuego(String juegoId, List<Directorio> nuevosDirectorios, String nuevoTitulo) {
        Tx.runVoid(() -> {
            Juego juegoBD = juegoRepository.findById(juegoId).orElseThrow();
            juegoBD.setTitulo(nuevoTitulo);
            List<Directorio> directoriosEliminados = juegoBD.getSaveFilePaths().stream().filter(d -> nuevosDirectorios.stream().noneMatch(directorio -> d.getPathPrincipal().equals(directorio.getPathPrincipal()))).toList();
            directoriosEliminados.forEach(archivoService::eliminarDatosDeDirectorio);
            juegoBD.getSaveFilePaths().removeIf(
                    d -> nuevosDirectorios.stream()
                            .noneMatch(p -> p.equals(d.getPathPrincipal()))
            );
            nuevosDirectorios.stream()
                    .filter(p -> juegoBD.getSaveFilePaths().stream()
                            .noneMatch(d -> d.getPathPrincipal().equals(p)))
                    .forEach(juegoBD::agregarDirectorio);
        });

    }
}

