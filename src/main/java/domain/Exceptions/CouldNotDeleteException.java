package domain.Exceptions;

import utils.I18nManager;

import java.io.File;

public class CouldNotDeleteException extends RuntimeException {
    public CouldNotDeleteException(File file) {
        super(I18nManager.get("CouldNotDeleteException", file.getAbsoluteFile().getAbsolutePath()));
    }
}
