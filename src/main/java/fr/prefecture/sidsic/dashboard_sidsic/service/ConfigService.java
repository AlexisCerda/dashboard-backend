package fr.prefecture.sidsic.dashboard_sidsic.service;

import fr.prefecture.sidsic.dashboard_sidsic.dto.AppConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class ConfigService {

    private final ObjectMapper objectMapper;
    private final String FILE_PATH = "config.json";

    public ConfigService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public AppConfig getConfiguration() {
        try {
            File fichierJson = new File(FILE_PATH);
            if (!fichierJson.exists()) {
              throw new FileNotFoundException("CRITIQUE : Le fichier config.json est introuvable à la racine du serveur !");
            }
            return objectMapper.readValue(fichierJson, AppConfig.class);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier de configuration", e);
        }
    }
    public void updateConfiguration(AppConfig nouvelleConfig) {
        try {
            File fichierJson = new File(FILE_PATH);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(fichierJson, nouvelleConfig);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde de la configuration", e);
        }
    }
}