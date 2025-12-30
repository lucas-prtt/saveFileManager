import domain.Archivos.checkpoint.Carpeta;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadWriteAndDelete {
    //Copiar TestsResources al disco C
    public static void main(String[] args) throws Exception {
        Carpeta miArchivo = new Carpeta("DirectorioEjemplo");
        Path desde = Paths.get("C:", "TestsResources").toAbsolutePath();
        System.out.println("Directorio desde " + desde);
        miArchivo.cargarArchivoDe(desde);
        Path hasta = desde.resolve("OtroDirectorio");
        System.out.println("Directorio hasta " + hasta);
        System.out.println("Se copio el archivo:");
        System.out.println(miArchivo.tree());
        miArchivo.escribirEn(hasta);
        miArchivo.borrar(hasta);
    }
}
