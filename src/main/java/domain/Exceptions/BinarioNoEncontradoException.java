package domain.Exceptions;

import domain.Archivos.checkpoint.ArchivoFinal;
import domain.Archivos.checkpoint.Binario;
import utils.I18nManager;

public class BinarioNoEncontradoException extends RuntimeException {
    public BinarioNoEncontradoException(ArchivoFinal archivoFinal) {
        super(I18nManager.get("BinarioNoEncontradoException", archivoFinal.getBinario().getHash()));
    }
    public BinarioNoEncontradoException(Binario binario) {
        super(I18nManager.get("BinarioNoEncontradoException", binario.getHash()));
    }
}
