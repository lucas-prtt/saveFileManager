package domain.Archivos;

import jakarta.persistence.AttributeConverter;
import java.nio.file.Path;
import java.nio.file.Paths;
// Se usa para convertir automaticamente Path en String y viceversa cuando se persisten Directorios
public class PathConverter implements AttributeConverter<Path, String> {

    @Override
    public String convertToDatabaseColumn(Path path) {
        return path != null ? path.toString() : null;
    }

    @Override
    public Path convertToEntityAttribute(String dbData) {
        return dbData != null ? Paths.get(dbData) : null;
    }
}