package Controladores;

import Juegos.Checkpoint;
import Juegos.CheckpointDTO;
import Juegos.Juego;
import Juegos.Partida;
import Repositories.CheckpointRepository;
import Repositories.JuegoRepository;
import Repositories.PartidaRepository;
import org.hibernate.annotations.Check;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/juegos")
public class RestController {
    private final JuegoRepository juegoRepository;
    private final PartidaRepository partidaRepository;
    private final CheckpointRepository checkpointRepository;

    public RestController(JuegoRepository juegoRepository, PartidaRepository partidaRepository, CheckpointRepository checkpointRepository) {
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

}
