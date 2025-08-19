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
        return ResponseEntity.ok(juegoService.titulosDeTodosLosJuegos());
    }

    @GetMapping("/{titulo}")
    public ResponseEntity<Juego> obtenerJuegoPorTitulo(@PathVariable String titulo){
        try {
            return ResponseEntity.ok(juegoService.obtenerJuegoPorTitulo(titulo));
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{titulo}/partidas")
    public ResponseEntity<List<String>> listarPartidasPorJuego(@PathVariable String titulo){
        try {
            return ResponseEntity.ok(juegoService.obtenerJuegoPorTitulo(titulo).getTitulosPartidas());
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{titulo}/partidas/{partida}")
    public ResponseEntity<Partida> obtenerPartidaPorJuegoYPartida(@PathVariable String titulo, @PathVariable String partida){
        try
        {return ResponseEntity.ok(partidaService.obtenerPartidaDeJuegoPorTitulo(titulo, partida));}
        catch (ResourceNotFoundException e)
        {return ResponseEntity.notFound().build();}
    }
    @GetMapping("/{titulo}/partidas/{partida}/checkpoints")
    public ResponseEntity<List<CheckpointDTO>> obtenerCheckpoints(@PathVariable String titulo, @PathVariable String partida){
        try
        {return ResponseEntity.ok(partidaService.obtenerPartidaDeJuegoPorTitulo(titulo, partida).getCheckpointsDTO());}
        catch (ResourceNotFoundException e)
        {return ResponseEntity.notFound().build();}
    }
    @GetMapping("/{titulo}/partidas/{partida}/checkpoints/{checkpoint}") // No se incluye los archivos
    public ResponseEntity<Checkpoint> obtenerCheckpoint(@PathVariable String titulo, @PathVariable String partida, @PathVariable String checkpoint){
        try
        {return ResponseEntity.ok(checkpointService.obtenerCheckpointPorJuegoPartidaYUuid(titulo, partida, checkpoint));}
        catch (ResourceNotFoundException e)
        {return ResponseEntity.notFound().build();}
    }
    @GetMapping("/{titulo}/partidas/{partida}/checkpoints/{checkpoint}/archivos")
    public ResponseEntity<List<Archivo>> obtenerArchivosCheckpoint(@PathVariable String titulo, @PathVariable String partida, @PathVariable String checkpoint){
        try
        {return ResponseEntity.ok(checkpointService.obtenerCheckpointPorJuegoPartidaYUuid(titulo, partida, checkpoint).getArchivos());}
        catch (ResourceNotFoundException e)
        {return ResponseEntity.notFound().build();}
    }

    @PostMapping
    public ResponseEntity<Juego> postJuego(@RequestBody Juego juego){
        System.out.println("Post de juego recibido");
        try {
            juegoService.guardarNuevoJuego(juego);
            return ResponseEntity.ok(juego);
        }catch (ResourceAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/{juego}/partidas")
    public ResponseEntity<Partida> postPartida(@PathVariable String juego, @RequestBody Partida partida){
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
        System.out.println("Post de checkpoint recibido");
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
        try {
            juegoService.eliminarJuego(titulo);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{titulo}/partidas/{partida}")
    public ResponseEntity<?> eliminarPartidaPorTitulo(@PathVariable String titulo, @PathVariable String partida){
        try {
            partidaService.eliminarPartida(titulo, partida);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{titulo}/partidas/{partida}/checkpoints/{checkpoint}")
    public ResponseEntity<?> eliminarCheckpointPorTituloPartidaYUUID(@PathVariable String titulo, @PathVariable String partida, @PathVariable String checkpoint){
        try{
            checkpointService.eliminarCheckpoint(titulo, partida, checkpoint);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/{titulo}")
    public ResponseEntity<Juego> patchJuego(@PathVariable String titulo, @RequestBody JuegoPatchDTO juegoDTO){
        try {
            juegoService.patchJuegoWithDTO(titulo, juegoDTO);
            return ResponseEntity.ok(juegoService.obtenerJuegoPorTitulo(titulo));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/{titulo}/partidas/{partida}")
    public ResponseEntity<Partida> partchPartida( @PathVariable String tituloJuego, @PathVariable String partida, @RequestBody PartidaPatchDTO patch){
        try {
            partidaService.patchPartida(tituloJuego, partida, patch);
            return ResponseEntity.ok(partidaService.obtenerPartidaDeJuegoPorTitulo(tituloJuego, partida));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

}
