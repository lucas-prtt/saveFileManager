package ui.tabs;

import domain.Juegos.Juego;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public abstract class TabFactory {

    @Lookup
    public abstract TabGestionarJuego crearTab(Juego juego);
}