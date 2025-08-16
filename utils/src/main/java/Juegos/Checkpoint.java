package Juegos;

import java.time.LocalDateTime;

public class Checkpoint {
    LocalDateTime fechaDeCreacion;
    String descripcion;
    public Checkpoint(){
        this.fechaDeCreacion = LocalDateTime.now();
    }
    public Checkpoint(String descripcion){
        this.descripcion = descripcion;
        this.fechaDeCreacion = LocalDateTime.now();
    }
    public String getDescripcion(){
        return descripcion;
    }
    public LocalDateTime getFechaDeCreacion(){
        return fechaDeCreacion;
    }
    public String getStringReferencia(){
        if(descripcion == null)
            return fechaDeCreacion.toString();
        else
            return fechaDeCreacion.toString() + " - " + descripcion;
    }
}
