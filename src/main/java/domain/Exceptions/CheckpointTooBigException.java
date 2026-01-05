package domain.Exceptions;

import utils.Converter;

public class CheckpointTooBigException extends RuntimeException {
    public CheckpointTooBigException(Long size, Long max) {
        super("No se puede guardar el checkpoint. El tamaño del checkpoint seria de " + Converter.formatBytes(size) + " mientras que el maximo especificado es de " + Converter.formatBytes(max));
    }
}
