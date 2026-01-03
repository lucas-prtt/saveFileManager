package domain.Exceptions;

import lombok.Getter;

@Getter
public class ArchivoYaExisteException extends Exception {
  private final String hash;
    public ArchivoYaExisteException(String hash) {
        super("Ya existe un binario con el hash: " + hash);
        this.hash = hash;
    }
}
