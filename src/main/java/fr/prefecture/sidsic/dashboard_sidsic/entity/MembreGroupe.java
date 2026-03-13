package fr.prefecture.sidsic.dashboard_sidsic.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MembreGroupe {
    public static final int ROLE_INVITE = 0;
    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_MEMBRE = 2;

    public MembreGroupe() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private int version;

    @ManyToOne
    @JoinColumn(name = "idmembre")
    private Membre membre;
    @ManyToOne
    @JoinColumn(name = "IDgroupe")
    private Groupe groupe;

    private int IsAdmin;

    public int getRole() {
        return IsAdmin;
    }

    public void setRole(int role) {
        this.IsAdmin = role;
    }

}
