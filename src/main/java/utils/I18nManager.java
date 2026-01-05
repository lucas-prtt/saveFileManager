package utils;

import com.opencsv.CSVReader;
import lombok.Getter;
import servicios.DirectorySecurity;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class I18nManager {
    @Getter
    private static String lang = "es";
    private static final Path configFile = Paths.get("data/languageConfig.json");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void setLang(String lang){
        I18nManager.lang = lang;
        saveConfig();
    }

    private static final Map<String, Map<String, String>> MESSAGES = new HashMap<>();
    private static final String DEFAULT_LANG = "es";
    static {
        loadMessages("translations.csv");
    }

    public static List<String> getLanguageKeys(){
        return MESSAGES.get("language").keySet().stream().sorted().collect(Collectors.toList());
    }
    private static void loadMessages(String resourcePath) {
        try (InputStream input = I18nManager.class.getClassLoader().getResourceAsStream(resourcePath);
             InputStreamReader reader = new InputStreamReader(input);
             CSVReader csvReader = new CSVReader(reader)) {

            String[] headers = csvReader.readNext();
            if (headers == null) return;

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                String key = line[0];
                Map<String, String> translations = new HashMap<>();
                for (int i = 1; i < line.length; i++) {
                    translations.put(headers[i].trim(), line[i].trim());
                }
                MESSAGES.put(key, translations);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getWithLang(String key, String lang, Object... args) {
        String template = MESSAGES.getOrDefault(key, Collections.emptyMap())
                .getOrDefault(lang,
                        MESSAGES.getOrDefault(key, Collections.emptyMap())
                                .getOrDefault(DEFAULT_LANG, key)
                );

        return new MessageFormat(template, getLocaleForLanguage(lang)).format(args);
    }
    public static String getLanguageEmoji(String lang) {
        return getWithLang("languageEmoji", lang);
    }
    public static String getLanguageFullWord(String lang) {
        return getWithLang("language", lang);
    }

    public static String get(String key, Object... args) {
        String template = MESSAGES.getOrDefault(key, Collections.emptyMap())
                .getOrDefault(lang,
                        MESSAGES.getOrDefault(key, Collections.emptyMap())
                                .getOrDefault(DEFAULT_LANG, key)
                );

        return new MessageFormat(template, getLocaleForLanguage(lang)).format(args);
    }
    public static Locale getLocaleForLanguage(String lang) {
        return switch (lang) {
            case "es" -> Locale.forLanguageTag("es-AR");
            case "en" -> Locale.US;
            case "de" -> Locale.GERMANY;
            case "pt" -> Locale.forLanguageTag("pt-BR");
            case "fr" -> Locale.FRANCE;
            case "it" -> Locale.ITALY;
            default -> Locale.US;
        };
    }
    public static String capitalize(String s) {
        return (s == null || s.isEmpty()) ? s : s.substring(0,1).toUpperCase() + s.substring(1);
    }


    public static void loadConfig() {
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

        I18nManager.ConfigData data = objectMapper.readValue(configFile.toFile(), I18nManager.ConfigData.class);
        lang = data.lang;
        System.out.println("Config cargada de " + configFile.toAbsolutePath().toString());

    }

    private static void saveConfig() {
        try {
            I18nManager.ConfigData data = new I18nManager.ConfigData();
            data.lang =lang;
            if (!Files.exists(configFile.getParent())) {
                System.out.println("Config ya existe");
                Files.createDirectories(configFile.getParent());
            }

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(configFile.toFile(), data);
            System.out.println("Config guardada en " + configFile.toAbsolutePath().toString());
        } catch (IOException e) {
            throw new RuntimeException(I18nManager.get("ErrorGuardandoConfiguracionDeLenguaje", e.getMessage()), e);
        }
    }
    private static void configurarPorDefecto(){
        lang = "en";
    }

    private static class ConfigData {
        public String lang;
    }
}