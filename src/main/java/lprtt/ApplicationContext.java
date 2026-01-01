package lprtt;

import repositorios.CheckpointRepository;
import repositorios.JuegoRepository;
import repositorios.PartidaRepository;
import servicios.DirectorySecurity;
import lombok.Getter;
import servicios.CheckpointService;
import servicios.FileManager;
import servicios.JuegosService;
import servicios.PartidaService;
import ui.MainController;
import utils.JpaUtil;

public class ApplicationContext {
    @Getter
    private static MainController mainController;
    @Getter
    private static CheckpointService checkpointService;
    @Getter
    private static FileManager fileManager;
    @Getter
    private static JuegosService juegosService;
    @Getter
    private static PartidaService partidaService;
    @Getter
    private static DirectorySecurity directorySecurity;
    @Getter
    private static JuegoRepository juegoRepository;
    @Getter
    private static PartidaRepository partidaRepository;
    @Getter
    private static CheckpointRepository checkpointRepository;

    public static void init(){
        checkpointRepository = new CheckpointRepository();
        partidaRepository = new PartidaRepository();
        juegoRepository= new JuegoRepository();
        directorySecurity = new DirectorySecurity();
        partidaService = new PartidaService(partidaRepository);
        juegosService = new JuegosService(juegoRepository, partidaService);
        fileManager = new FileManager(directorySecurity);
        checkpointService = new CheckpointService(checkpointRepository, juegosService, partidaService, fileManager);
        mainController = new MainController(juegosService, partidaService, checkpointService, directorySecurity);
    }

}
