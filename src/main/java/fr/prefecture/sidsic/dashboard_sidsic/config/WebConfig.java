package fr.prefecture.sidsic.dashboard_sidsic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // On récupère le chemin absolu de la racine de ton projet (là où est le dossier uploads)
        String projectRoot = System.getProperty("user.dir");
        String uploadDir = projectRoot + File.separator + "uploads" + File.separator;

        // On dit à Spring Boot : "Si on te demande une URL commençant par /uploads/..."
        // "... va chercher le fichier correspondant dans ce dossier physique sur le PC."
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir);
    }
}
