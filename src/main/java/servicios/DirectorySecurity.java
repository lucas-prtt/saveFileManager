package servicios;

import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
public class DirectorySecurity {
    private final Path configFile = Paths.get("data/directorySecurity.json");
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Set<Path> whitelist = new HashSet<>();
    private final Set<Path> blacklist = new HashSet<>();
    private boolean whitelistOverridesBlackList = true;
    public DirectorySecurity() {
        loadConfig();
    }

    public void setWhitelistOverridesBlackList(boolean whitelistOverridesBlackList) {
        this.whitelistOverridesBlackList = whitelistOverridesBlackList;
        saveConfig();
    }

    public boolean isValid(Path path) {
        Path absoluto = path.toAbsolutePath().normalize();
        if(whitelistOverridesBlackList && isInWhitelist(absoluto))
            return true;
        if(isInBlacklist(absoluto))
            return false;
        return isInWhitelist(absoluto);
    }

    public boolean isInWhitelist(Path path) {
        for (Path permitido : whitelist) {
            if (path.startsWith(permitido)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInBlacklist(Path path) {
        for (Path permitido : blacklist) {
            if (path.startsWith(permitido)) {
                return true;
            }
        }
        return false;
    }

    public void addToWhitelist(Path path) {
        whitelist.add(path.toAbsolutePath().normalize());
        saveConfig();
    }

    public void addToBlacklist(Path path) {
        blacklist.add(path.toAbsolutePath().normalize());
        saveConfig();
    }

    public void removeFromWhitelist(Path path) {
        whitelist.remove(path);
        saveConfig();
    }

    public void removeFromBlacklist(Path path) {
        blacklist.add(path);
        saveConfig();
    }


    public void validarRuta(Path ruta){
        if(Files.isSymbolicLink(ruta.toAbsolutePath().normalize())){
            try {
                validarRuta(ruta.toAbsolutePath().normalize().toRealPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(!isValid(ruta.toAbsolutePath().normalize()))
            throw new RuntimeException("Se intento acceder a un directorio no permitido: " + ruta);
    }

    private void loadConfig() {
        if (!Files.exists(configFile)){
            if (!Files.exists(configFile.getParent())) {
                try {
                    Files.createDirectories(configFile.getParent());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            configurarPorDefecto();
            saveConfig();
            return;
        };

        ConfigData data = objectMapper.readValue(configFile.toFile(), ConfigData.class);
        whitelist.clear();
        blacklist.clear();
        whitelistOverridesBlackList = data.whitelistOverridesBlackList;

        data.whitelist.forEach(p -> whitelist.add(Paths.get(p).toAbsolutePath().normalize()));
        data.blacklist.forEach(p -> blacklist.add(Paths.get(p).toAbsolutePath().normalize()));
        System.out.println("Config cargada de " + configFile.toAbsolutePath().toString());

    }

    private void saveConfig() {
        try {
            ConfigData data = new ConfigData();
            data.whitelistOverridesBlackList = whitelistOverridesBlackList;
            whitelist.forEach(p -> data.whitelist.add(p.toAbsolutePath().normalize().toString()));
            blacklist.forEach(p -> data.blacklist.add(p.toAbsolutePath().normalize().toString()));

            if (!Files.exists(configFile.getParent())) {
                System.out.println("Config ya existe");
                Files.createDirectories(configFile.getParent());
            }

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(configFile.toFile(), data);
            System.out.println("Config guardada en " + configFile.toAbsolutePath().toString());
        } catch (IOException e) {
            throw new RuntimeException("Error guardando configuración de directorios", e);
        }
    }
    private void configurarPorDefecto(){
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")) {
            blacklist.add(Paths.get("C:\\Windows"));
            blacklist.add(Paths.get("C:\\Program Files"));
            blacklist.add(Paths.get("C:\\Program Files (x86)"));
            blacklist.add(Paths.get("C:\\Users"));
            blacklist.add(Paths.get("C:\\"));
        } else {
            blacklist.add(Paths.get("/"));
            blacklist.add(Paths.get("/bin"));
            blacklist.add(Paths.get("/usr"));
            blacklist.add(Paths.get("/etc"));
            blacklist.add(Paths.get("/var"));
            blacklist.add(Paths.get("/home"));
        }
        whitelistOverridesBlackList=true;
    }

    private static class ConfigData {
        public Set<String> whitelist = new HashSet<>();
        public Set<String> blacklist = new HashSet<>();
        public boolean whitelistOverridesBlackList = true;
    }
}