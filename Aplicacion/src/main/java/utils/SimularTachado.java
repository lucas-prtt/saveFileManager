package utils;

public class SimularTachado {
    public static String tachar(String texto) {
        StringBuilder tachado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            tachado.append(c).append('\u0336');
        }
        return tachado.toString();
    }
}
