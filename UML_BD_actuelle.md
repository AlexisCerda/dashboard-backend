# Diagramme UML/ER - BD actuelle

```mermaid
erDiagram
    GROUPE {
        LONG id PK
        INT version
        STRING nom
        STRING ville
    }

    MEMBRE {
        LONG id PK
        INT version
        STRING email
        STRING password
        LOCALDATE LastConnection
        STRING Nom
        STRING Prenom
        LONG CurrentGroupe FK
    }

    MEMBRE_GROUPE {
        LONG id PK
        INT version
        LONG idmembre FK
        LONG IDgroupe FK
        INT IsAdmin
    }

    TACHE {
        LONG id PK
        INT version
        LONG IDgroupe FK
        STRING nom
        STRING Description
        LOCALDATE dateDebut
        LOCALDATE dateLimite
        ETAT_TACHE etat
    }

    TACHE2_MEMBRE {
        LONG IDmembre PK, FK
        LONG IDtache PK, FK
    }

    ACHAT {
        LONG id PK
        INT version
        LONG IDgroupe FK
        INT quantite
        STRING NomPersonne
        STRING PrenomPersonne
        ETAT_ACHAT etat
        STRING NomMateriel
        STRING MarqueMateriel
        STRING Reference
    }

    PRET {
        LONG id PK
        INT version
        LONG IDgroupe FK
        STRING NomMateriel
        STRING MarqueMateriel
        STRING NomPersonne
        STRING PrenomPersonne
        INT quantite
        ETAT_PRET etat
        LOCALDATE dateDebut
        LOCALDATE dateFin
    }

    MOUVEMENT {
        LONG id PK
        INT version
        LONG IDgroupe FK
        ETAT_MOUVEMENT etat
        STRING nom
        STRING prenom
        LOCALDATE dateArrivee
        LOCALDATE dateDepart
    }

    NOTE {
        LONG id PK
        INT version
        LONG IDmembre FK
        STRING description
    }

    CONFIGURATION {
        LONG id PK
        INT version
        LONG IDmembre FK
        LONG IDgroupe FK
        STRING Nom
        STRING Taches
        STRING Notes
        STRING Achats
        STRING Prets
        STRING Mouvements
    }

    ETAT_TACHE {
        STRING value PK
    }

    ETAT_ACHAT {
        STRING value PK
    }

    ETAT_PRET {
        STRING value PK
    }

    ETAT_MOUVEMENT {
        STRING value PK
    }

    GROUPE ||--o{ ACHAT : "possede"
    GROUPE ||--o{ PRET : "possede"
    GROUPE ||--o{ MOUVEMENT : "possede"
    GROUPE ||--o{ TACHE : "possede"
    GROUPE ||--o{ CONFIGURATION : "parametre"

    GROUPE ||--o{ MEMBRE : "CurrentGroupe"

    MEMBRE ||--o{ NOTE : "ecrit"
    MEMBRE ||--o{ CONFIGURATION : "personnalise"

    MEMBRE ||--o{ MEMBRE_GROUPE : "appartenance"
    GROUPE ||--o{ MEMBRE_GROUPE : "appartenance"

    MEMBRE ||--o{ TACHE2_MEMBRE : "assigne"
    TACHE ||--o{ TACHE2_MEMBRE : "assigne"

    ETAT_TACHE ||--o{ TACHE : "etat"
    ETAT_ACHAT ||--o{ ACHAT : "etat"
    ETAT_PRET ||--o{ PRET : "etat"
    ETAT_MOUVEMENT ||--o{ MOUVEMENT : "etat"
```

## Valeurs d'enum (code actuel)

- EtatTache: `à_FAIRE`, `EN_COURS`, `TERMINé`
- EtatAchat: `à_ACHETER`, `COMMANDé`, `REçU`
- EtatPret: `EN_COURS`, `RENDU`, `EN_RETARD`
- EtatMouvement: `STAGE`, `DéPART`, `ARRIVéE`
