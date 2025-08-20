package Controladores;

import Archivos.Archivo;
import Exceptions.ResourceAlreadyExistsException;
import Exceptions.ResourceNotFoundException;
import Juegos.*;
import Servicios.CheckpointService;
import Servicios.JuegosService;
import Servicios.PartidaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/juegos")
public class JuegosRestController {
    private final JuegosService juegoService;
    private final PartidaService partidaService;
    private final CheckpointService checkpointService;

    public JuegosRestController(JuegosService juegoService, PartidaService partidaService, CheckpointService checkpoint) {
        this.juegoService = juegoService;
        this.partidaService = partidaService;
        this.checkpointService = checkpoint;
    }

    @GetMapping
    public ResponseEntity<List<String>> listarJuegos(){
        System.out.println("Patch juegos");
        return ResponseEntity.ok(juegoService.titulosDeTodosLosJuegos());
    }

    @GetMapping("/{titulo}")
    public ResponseEntity<Juego> obtenerJuegoPorTitulo(@PathVariable String titulo){
        System.out.println("Get juego");
        try {
            return ResponseEntity.ok(juegoService.obtenerJuegoPorTitulo(titulo));
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{titulo}/partidas")
    public ResponseEntity<List<String>> listarPartidasPorJuego(@PathVariable String titulo){
        System.out.println("Get Partidas");
        try {
            return ResponseEntity.ok(juegoService.obtenerJuegoPorTitulo(titulo).getTitulosPartidas());
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{titulo}/partidas/{partida}")
    public ResponseEntity<Partida> obtenerPartidaPorJuegoYPartida(@PathVariable String titulo, @PathVariable String partida){
        System.out.println("Get Partida");
        try
        {return ResponseEntity.ok(partidaService.obtenerPartidaDeJuegoPorTitulo(titulo, partida));}
        catch (ResourceNotFoundException e)
        {return ResponseEntity.notFound().build();}
    }
    @GetMapping("/{titulo}/partidas/{partida}/checkpoints")
    public ResponseEntity<List<CheckpointDTO>> obtenerCheckpoints(@PathVariable String titulo, @PathVariable String partida){
        System.out.println("Get Checkpoints");
        try
        {return ResponseEntity.ok(partidaService.obtenerPartidaDeJuegoPorTitulo(titulo, partida).getCheckpointsDTO());}
        catch (ResourceNotFoundException e)
        {return ResponseEntity.notFound().build();}
    }
    @GetMapping("/{titulo}/partidas/{partida}/checkpoints/{checkpoint}") // No se incluye los archivos
    public ResponseEntity<Checkpoint> obtenerCheckpoint(@PathVariable String titulo, @PathVariable String partida, @PathVariable String checkpoint){
        System.out.println("Get Checkpoint");
        try
        {return ResponseEntity.ok(checkpointService.obtenerCheckpointPorJuegoPartidaYUuid(titulo, partida, checkpoint));}
        catch (ResourceNotFoundException e)
        {return ResponseEntity.notFound().build();}
    }
    @GetMapping("/{titulo}/partidas/{partida}/checkpoints/{checkpoint}/archivos")
    public ResponseEntity<List<Archivo>> obtenerArchivosCheckpoint(@PathVariable String titulo, @PathVariable String partida, @PathVariable String checkpoint){
        System.out.println("Get Archivos");
        try
        {return ResponseEntity.ok(checkpointService.obtenerCheckpointPorJuegoPartidaYUuid(titulo, partida, checkpoint).getArchivos());}
        catch (ResourceNotFoundException e)
        {return ResponseEntity.notFound().build();}
    }

    @PostMapping
    public ResponseEntity<Juego> postJuego(@RequestBody Juego juego){
        System.out.println("Post Juego");
        try {
            juegoService.guardarNuevoJuego(juego);
            return ResponseEntity.ok(juego);
        }catch (ResourceAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/{juego}/partidas")
    public ResponseEntity<Partida> postPartida(@PathVariable String juego, @RequestBody Partida partida){
        System.out.println("Post Partida");
        try {
            partidaService.guardarPartida(juego, partida);
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(partida);
    }
    @PostMapping("/{juego}/partidas/{partida}/checkpoints")
    public ResponseEntity<Checkpoint> postPartida(@PathVariable String juego, @PathVariable String partida,@RequestBody Checkpoint checkpoint){
        System.out.println("Post Checkpoint");
        try {
            checkpointService.guardarNuevoCheckpoint(juego, partida, checkpoint);
        }catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch (ResourceAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(checkpoint);
    }
    @DeleteMapping("/{titulo}")
    public ResponseEntity<?> eliminarJuegoPorTitulo(@PathVariable String titulo){
        System.out.println("Delete juego");
        try {
            juegoService.eliminarJuego(titulo);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{titulo}/partidas/{partida}")
    public ResponseEntity<?> eliminarPartidaPorTitulo(@PathVariable String titulo, @PathVariable String partida){
        System.out.println("Delete partida");
        try {
            partidaService.eliminarPartida(titulo, partida);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{titulo}/partidas/{partida}/checkpoints/{checkpoint}")
    public ResponseEntity<?> eliminarCheckpointPorTituloPartidaYUUID(@PathVariable String titulo, @PathVariable String partida, @PathVariable String checkpoint){
        System.out.println("Delete checkpoint");

        try{
            checkpointService.eliminarCheckpoint(titulo, partida, checkpoint);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/{titulo}")
    public ResponseEntity<Juego> patchJuego(@PathVariable String titulo, @RequestBody JuegoPatchDTO juegoDTO){
        System.out.println("Patch juego");
        try {
            juegoService.patchJuegoWithDTO(titulo, juegoDTO);
            Juego jue = juegoService.obtenerJuegoPorTitulo(titulo);
            return ResponseEntity.ok(jue);
        }catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/{titulo}/partidas/{partida}")
    public ResponseEntity<Partida> partchPartida( @PathVariable String tituloJuego, @PathVariable String partida, @RequestBody PartidaPatchDTO patch){
        System.out.println("Patch partida");
        try {
            partidaService.patchPartida(tituloJuego, partida, patch);
            Partida ptd = partidaService.obtenerPartidaDeJuegoPorTitulo(tituloJuego, partida);
            return ResponseEntity.ok(ptd);
        }catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

}
