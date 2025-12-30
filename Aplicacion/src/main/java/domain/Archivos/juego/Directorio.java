package domain.Archivos.juego;

import domain.Archivos.PathConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Directorio {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Convert(converter = PathConverter.class)
    Path pathPrincipal;
    @ElementCollection
    @Convert(converter = PathConverter.class)
    List<Path> pathsSinonimos = new ArrayList<>();

    public Directorio(Path pathPrincipal){
        this.pathPrincipal = pathPrincipal;
    }
}
