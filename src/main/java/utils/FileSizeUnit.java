package utils;

enum FileSizeUnit {
    B(1L),
    KB(1024L),
    MB(1024L * 1024L),
    GB(1024L * 1024L * 1024L);

    private final long factor;

    FileSizeUnit(long factor) {
        this.factor = factor;
    }

    long toBytes(long value) {
        return value * factor;
    }

    long fromBytes(long bytes) {
        return bytes / factor;
    }

    static FileSizeUnit bestFit(long bytes) {
        if (bytes >= GB.factor) return GB;
        if (bytes >= MB.factor) return MB;
        if (bytes >= KB.factor) return KB;
        return B;
    }
}
