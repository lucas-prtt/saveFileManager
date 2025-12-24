package domain.Exceptions;

public class ResourceAlreadyExistsException extends RuntimeException{
    public ResourceAlreadyExistsException(String mensaje){
        super(mensaje);
    }
}
