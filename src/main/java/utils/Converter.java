package utils;

public class Converter {
    public static String formatBytes(long bytes) {
        FileSizeUnit unit = FileSizeUnit.bestFit(bytes);
        double value = unit.fromBytes(bytes);
        return String.format("%.2f %s", value, unit.name());
    }
}
