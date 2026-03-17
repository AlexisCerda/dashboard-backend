package fr.prefecture.sidsic.dashboard_sidsic.entity;
import java.util.ArrayList;
import java.util.List;

import fr.prefecture.sidsic.dashboard_sidsic.dto.TacheDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Groupe {
    public Groupe(){}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private int version;

    private String nom;
    private String ville;
    

    @OneToMany(mappedBy = "groupe",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MembreGroupe> membres;

    @OneToMany(mappedBy = "Current_groupe")
    private List<Membre> membres_current;

    @OneToMany(mappedBy = "groupe" , cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Achat> achats;

    @OneToMany(mappedBy = "groupe" , cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Pret> prets;

    @OneToMany(mappedBy = "groupe" , cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Mouvement> mouvements;

    @OneToMany(mappedBy = "groupe" , cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Tache> taches;

    @OneToMany(mappedBy = "groupe" , cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Configuration> configurations;

    public List<TacheDTO> getTaches() {
        List<Tache> taches = this.taches;
        List<TacheDTO> tachesDTO = new ArrayList<>();
        for (Tache tache : taches) {
            TacheDTO tacheDTO = new TacheDTO();
            tacheDTO.setId(tache.getId());
            tacheDTO.setNom(tache.getNom());
            tacheDTO.setDescription(tache.getDescription());
            tacheDTO.setDateDebut(tache.getDateDebut());
            tacheDTO.setDateLimite(tache.getDateLimite());
            tachesDTO.add(tacheDTO);
        }
        return tachesDTO;
    }
}

