package fr.prefecture.sidsic.dashboard_sidsic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppConfig {
    private String emailAdmin;
    private int maxTaches;
    private int maxGroupes;
    
    public AppConfig() {}
}