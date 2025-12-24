package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartidaDTO {
    String tituloJuego;
    String id;
    String tituloPartida;
    List<String> checkpointsIDs;
    public void generateNewId(){
        id = UUID.randomUUID().toString();
    }

}
