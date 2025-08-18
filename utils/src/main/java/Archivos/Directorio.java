package Archivos;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Directorio {
    // Clase encargada de manejar sinonimos para Paths
    @Id
    String id;
    @Convert(converter = PathConverter.class)
    Path pathPrincipal;
    @Convert(converter = PathConverter.class)
    List<Path> pathsSinonimos = new ArrayList<>();

    public Directorio(Path pathPrincipal, List<Path> pathsSinonimos){
        id = UUID.randomUUID().toString();
        pathsSinonimos.forEach(this::addPathAsSynonim);
        setPathPrincipal(pathPrincipal);
    }
    public Directorio(String pathPrincipal, List<String> pathsSinonimos){
        id = UUID.randomUUID().toString();
        pathsSinonimos.forEach(this::addPathAsSynonimFromString);
        setPathPrincipalFromString(pathPrincipal);
    }
    public Directorio(String pathPrincipal){
        id = UUID.randomUUID().toString();
        setPathPrincipalFromString(pathPrincipal);
    }
    public Directorio(Path pathPrincipal){
        id = UUID.randomUUID().toString();
        setPathPrincipal(pathPrincipal);
    }
    public Path pathFromString(String rutaTexto){
        return Paths.get(rutaTexto);
    }
    public void setPathPrincipalFromString(String rutaTexto){
        setPathPrincipal(pathFromString(rutaTexto));
    }
    public void addPathAsSynonimFromString(String rutaTexto){
        addPathAsSynonim(pathFromString(rutaTexto));
    }
    public void setPathPrincipal(Path ruta){
        pathPrincipal = ruta;
        if(!pathsSinonimos.contains(ruta))
            pathsSinonimos.add(ruta);
    }
    public void addPathAsSynonim(Path ruta){
        if(!pathsSinonimos.contains(ruta))
            pathsSinonimos.add(ruta);
    }
    public void eliminarPath(Path ruta) throws Exception {
        if(pathPrincipal == ruta){
            throw new Exception("Path no puede ser eliminado, primero debe ser quitado del path principal");
        }
        pathsSinonimos.remove(ruta);
    }
    public void eliminarPathFromString(String ruta) throws Exception {
        eliminarPath(pathFromString(ruta));
    }
    public boolean principalEquivalenteA(Path ruta){
        return pathsEquivalentes(ruta, pathPrincipal);
    }
    public boolean pathsEquivalentes(Path ruta1, Path ruta2){
        try {
            return Files.isSameFile(ruta1, ruta2);
        }catch (Exception e){return false;}
    }
    public boolean principalEquivalenteAFromString(String ruta){
        return principalEquivalenteA(pathFromString(ruta));
    }
    public Path findMostLikelyPath() throws Exception {
        try {
            return findBestMatchingPath();
        }catch (Exception _){
            return AlgoritmoPredictivoPaths.predecir(pathsSinonimos);
        }
    }
    public Path findBestMatchingPath() throws Exception{
        if (pathExiste(pathPrincipal)){
            return pathPrincipal;
        }
        Optional<Path> path = pathsSinonimos.stream().filter(this::pathExiste).findFirst();
        if(path.isEmpty())
            throw new Exception("No hay path valido");
        return path.get();
    }
    public boolean pathExiste(Path path){
        return path.toFile().exists();
    }
}
