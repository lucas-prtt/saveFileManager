package lprtt;

import domain.Archivos.ObjectStore;
import repositorios.ArchivoRepository;
import repositorios.CheckpointRepository;
import repositorios.JuegoRepository;
import repositorios.PartidaRepository;
import servicios.DirectorySecurity;
import lombok.Getter;
import servicios.CheckpointService;
import servicios.ArchivoService;
import servicios.JuegosService;
import servicios.PartidaService;
import ui.MainController;
import utils.JpaUtil;

import java.nio.file.Paths;

public class ApplicationContext {
    @Getter
    private static MainController mainController;
    @Getter
    private static CheckpointService checkpointService;
    @Getter
    private static ArchivoService archivoService;
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
    @Getter
    private static ObjectStore objectStore;
    @Getter
    private static ArchivoRepository archivoRepository;

    public static void init(){
        checkpointRepository = new CheckpointRepository();
        partidaRepository = new PartidaRepository();
        juegoRepository= new JuegoRepository();
        archivoRepository = new ArchivoRepository();

        directorySecurity = new DirectorySecurity();
        partidaService = new PartidaService(partidaRepository);
        juegosService = new JuegosService(juegoRepository, partidaService);
        objectStore = new ObjectStore(Paths.get("data/objects"), directorySecurity);
        archivoService = new ArchivoService(directorySecurity, objectStore, archivoRepository);
        checkpointService = new CheckpointService(checkpointRepository, juegosService, partidaService, archivoService);

        mainController = new MainController(juegosService, partidaService, checkpointService, directorySecurity);
        JpaUtil.em();
    }

}
