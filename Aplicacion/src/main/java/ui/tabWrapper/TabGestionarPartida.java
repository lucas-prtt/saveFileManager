package ui.tabWrapper;

import domain.Juegos.Checkpoint;
import domain.Juegos.Partida;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import servicios.CheckpointService;
import servicios.PartidaService;
import ui.MainController;

import java.util.List;

public class TabGestionarPartida extends TabWrapper{
    private CheckpointService checkpointService;
    private PartidaService partidaService;
    private Partida partida;

    @Override
    public void init(MainController mainController) {
        super.init(mainController);
        this.partidaService = controller.getPartidaService();
        this.checkpointService = controller.getCheckpointService();
    }

    public TabGestionarPartida(Partida partida){
        super();
        this.partida = partida;
    }
    @Override
    public VBox getContent() {
        List<Checkpoint> checkpoints = partidaService.obtenerCheckpoints(partida);
        return new VBox(new Label(checkpoints.stream().map(Object::toString).toList().toString()));
    }

    @Override
    public String getName() {
        return partida.getJuego().getTitulo() + "  -  " +  partida.getTitulo();
    }
}
