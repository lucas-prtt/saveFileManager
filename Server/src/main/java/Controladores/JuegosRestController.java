package Controladores;

import Juegos.*;
import Repositorios.CheckpointRepository;
import Repositorios.JuegoRepository;
import Repositorios.PartidaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/juegos")
public class JuegosRestController {
    private final JuegoRepository juegoRepository;
    private final PartidaRepository partidaRepository;
    private final CheckpointRepository checkpointRepository;

    public JuegosRestController(JuegoRepository juegoRepository, PartidaRepository partidaRepository, CheckpointRepository checkpointRepository) {
        this.juegoRepository = juegoRepository;
        this.partidaRepository = partidaRepository;
        this.checkpointRepository = checkpointRepository;
    }

    @GetMapping
    public ResponseEntity<List<String>> listarJuegos(){
        List<String> juegos =  juegoRepository.findAll().stream().map(Juego::getTitulo).toList();
        return ResponseEntity.ok(juegos);
    }
    @GetMapping("/{titulo}")
    public ResponseEntity<Juego> obtenerJuegoPorTitulo(@PathVariable String titulo){
        Optional<Juego> juego =  juegoRepository.findById(titulo);
        if(juego.isPresent())
            return ResponseEntity.ok(juego.get());
        else
            return ResponseEntity.notFound().build();
    }
    @GetMapping("/{titulo}/partidas")
    public ResponseEntity<List<String>> listarPartidasPorJuego(@PathVariable String titulo){
        final Optional<Juego> juego = juegoRepository.findById(titulo);
        if (juego.isPresent())
            return ResponseEntity.ok(juego.get().getTitulosPartidas());
        else
            return ResponseEntity.notFound().build();
    }
    @GetMapping("/{titulo}/partidas/{partida}")
    public ResponseEntity<Partida> obtenerPartidaPorJuegoYPartida(@PathVariable String titulo, @PathVariable String partida){
        Optional<Juego> juegoObtenido = juegoRepository.findById(titulo);
        if(juegoObtenido.isEmpty())
            return ResponseEntity.notFound().build();
        Optional <Partida> partidaObtenida = juegoObtenido.get().getPartidaByTitulo(partida);
        if(partidaObtenida.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(partidaObtenida.get());
    }
    @GetMapping("/{titulo}/partidas/{partida}/checkpoints")
    public ResponseEntity<List<CheckpointDTO>> obtenerCheckpoints(@PathVariable String titulo, @PathVariable String partida){
        Optional<Juego> juegoObtenido = juegoRepository.findById(titulo);
        if(juegoObtenido.isEmpty())
            return ResponseEntity.notFound().build();
        Optional <Partida> partidaObtenida = juegoObtenido.get().getPartidaByTitulo(partida);
        if(partidaObtenida.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(partidaObtenida.get().getCheckpointsDTO());
    }
    @GetMapping("/{titulo}/partidas/{partida}/checkpoints/{checkpoint}")
    public ResponseEntity<Checkpoint> obtenerCheckpoints(@PathVariable String titulo, @PathVariable String partida, @PathVariable String checkpoint){
        Optional<Juego> juegoObtenido = juegoRepository.findById(titulo);
        if(juegoObtenido.isEmpty())
            return ResponseEntity.notFound().build();
        Optional <Partida> partidaObtenida = juegoObtenido.get().getPartidaByTitulo(partida);
        if(partidaObtenida.isEmpty())
            return ResponseEntity.notFound().build();
        Optional<Checkpoint> chk = partidaObtenida.get().getCheckpointById(checkpoint);
        if(chk.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chk.get());
    }

    @PostMapping
    public ResponseEntity<Juego> postJuego(@RequestBody Juego juego){
        System.out.println("Post de juego recibido");
        juegoRepository.save(juego);
        return ResponseEntity.ok(juego);
    }

    @PostMapping("/{juego}/partidas")
    public ResponseEntity<Partida> postPartida(@PathVariable String juego, @RequestBody Partida partida){
        System.out.println("Post de partida recibido");
        Optional<Juego> optj = juegoRepository.findById(juego);
        if (optj.isEmpty())
            return ResponseEntity.notFound().build();
        Juego j = optj.get();
        partida.setJuego(j);
        j.agregarPartida(partida);
        if (partida.getId() == null)
            partida.generateNewId();
        juegoRepository.save(j);
        return ResponseEntity.ok(partida);
    }
    @PostMapping("/{juego}/partidas/{partida}/checkpoints")
    public ResponseEntity<Checkpoint> postPartida(@PathVariable String juego, @PathVariable String partida,@RequestBody Checkpoint checkpoint){
        System.out.println("Post de checkpoint recibido");
        Optional<Juego> optj = juegoRepository.findById(juego);
        if (optj.isEmpty())
            return ResponseEntity.notFound().build();
        Juego j = optj.get();
        Optional<Partida> optp = j.getPartidaByTitulo(partida);
        if(optp.isEmpty())
            return ResponseEntity.notFound().build();
        Partida p = optp.get();
        checkpoint.setPartida(p);
        if (checkpoint.getId() == null)
            checkpoint.generateNewId();
        p.agregarCheckpoint(checkpoint);
        partidaRepository.save(p);
        return ResponseEntity.ok(checkpoint);
    }
    @DeleteMapping("/{titulo}")
    public ResponseEntity<?> eliminarJuegoPorTitulo(@PathVariable String titulo){
        Optional<Juego> juego =  juegoRepository.findById(titulo);
        if(juego.isPresent()) {
            juegoRepository.delete(juego.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else
            return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{titulo}/partidas/{partida}")
    public ResponseEntity<?> eliminarPartidaPorTitulo(@PathVariable String titulo, @PathVariable String partida){
        Optional<Juego> juego =  juegoRepository.findById(titulo);
        if(juego.isEmpty())
            return ResponseEntity.notFound().build();
        Juego j = juego.get();
        Optional<Partida> optp = j.getPartidaByTitulo(partida);
        if(optp.isEmpty())
            return ResponseEntity.notFound().build();
        j.eliminarPartida(optp.get());
        juegoRepository.save(j);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @DeleteMapping("/{titulo}/partidas/{partida}/checkpoints/{checkpoint}")
    public ResponseEntity<?> eliminarPartidaPorTitulo(@PathVariable String titulo, @PathVariable String partida, @PathVariable String checkpoint){
        Optional<Juego> juego =  juegoRepository.findById(titulo);
        if(juego.isEmpty())
            return ResponseEntity.notFound().build();
        Juego j = juego.get();
        Optional<Partida> optp = j.getPartidaByTitulo(partida);
        if(optp.isEmpty())
            return ResponseEntity.notFound().build();
        Partida p = optp.get();
        Optional<Checkpoint> optchk =  p.getCheckpointById(checkpoint);
        if(optchk.isEmpty())
            return ResponseEntity.notFound().build();
        Checkpoint chk = optchk.get();
        p.eliminarCheckpoint(chk);
        juegoRepository.save(j);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PatchMapping("/{titulo}")
    public ResponseEntity<Juego> patchJuego(@PathVariable String titulo, @RequestBody JuegoPatchDTO juegoDTO){
        System.out.println("Patch de juego recibido");
        Optional<Juego> optJuego = juegoRepository.findById(titulo);
        if (optJuego.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Juego j = optJuego.get();
        j.patchWithDto(juegoDTO);
        juegoRepository.save(j);
        return ResponseEntity.ok(j);
    }

}
