package Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Archivos.Archivo;

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
