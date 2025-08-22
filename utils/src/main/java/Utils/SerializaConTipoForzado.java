package Utils;

import Archivos.Archivo;
import Archivos.ArchivoFinal;
import Archivos.Carpeta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.springframework.aot.hint.TypeReference;

import java.util.List;

public class SerializaConTipoForzado {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String serializaAListaDeArchivos(List<Archivo> object) {
        try {
            return mapper.writerFor(new com.fasterxml.jackson.core.type.TypeReference<List<Archivo>>() {
            }).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
