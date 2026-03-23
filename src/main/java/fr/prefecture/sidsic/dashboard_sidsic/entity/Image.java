package fr.prefecture.sidsic.dashboard_sidsic.entity;

import fr.prefecture.sidsic.dashboard_sidsic.enums.EtatAchat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Image {
    public Image(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private int version;
    private String nom;
    private String path;
    @ManyToOne
    @JoinColumn(name = "IDmembre")
    private Membre membre;

}
