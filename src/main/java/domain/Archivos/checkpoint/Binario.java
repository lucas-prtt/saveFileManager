package domain.Archivos.checkpoint;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Binario {
    @Id
    @Column(length = 64)
    private String hash; //SHA 256
    private long size;
    private long usos;

    @Version
    private long version;
    public Binario(String hash,long size){
        this.size = size;
        this.hash = hash;
        this.usos = 0;
    }
    public void aumentarUso(){
        usos++;
    }
    public void reducirUso(){
        usos--;
    }
}
