
package fr.prefecture.sidsic.dashboard_sidsic.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Materiel {
    public Materiel(){}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private int version;
    private String nom;
    private String marque;

    @OneToMany(mappedBy = "materiel")
    List<Achat> achats;

    @OneToMany(mappedBy = "materiel")
    List<Pret> prets;
    
}
