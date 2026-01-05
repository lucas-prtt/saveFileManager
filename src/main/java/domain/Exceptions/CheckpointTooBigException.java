package domain.Exceptions;

import utils.Converter;
import utils.I18nManager;

public class CheckpointTooBigException extends RuntimeException {
    public CheckpointTooBigException(Long size, Long max) {
        super(I18nManager.get("CheckpointTooBigException", size, max));
    }
}
