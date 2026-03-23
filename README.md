# ⚙️ Dashboard SIDSIC - Back-End (API REST)

Bienvenue sur le dépôt Back-End du projet **Dashboard SIDSIC**.
Cette API, développée en Java avec Spring Boot, est le cœur logique de l'application. Elle gère la persistance des données, la sécurité, le stockage des fichiers physiques et la communication en temps réel (WebSockets) pour les bureaux virtuels collaboratifs de la Préfecture.

🔗 **Front-End associé (React) :** [https://github.com/AlexisCerda/dashboard-frontend](https://github.com/AlexisCerda/dashboard-frontend)



## ✨ Fonctionnalités Principales

### 🗄️ Gestion des Données & Configurations
* **Sauvegarde des Layouts :** Stockage des positions des widgets du bureau (coordonnées X, Y, W, H) au format JSON via des colonnes `LONGTEXT`.
* **Base de données intégrée :** Utilisation de H2 Database avec persistance sur fichier local (`.mv.db`) pour une portabilité maximale.

### 📁 Gestion Physique des Fichiers (Uploads)
* **API Multipart :** Endpoints dédiés à la réception de fichiers via `multipart/form-data`.
* **Stockage Local :** Enregistrement physique des images directement à la racine du serveur dans un dossier `/uploads/`.
* **Exposition des Ressources :** Configuration personnalisée via `WebMvcConfigurer` pour servir publiquement les images statiques au Front-End.
* **Nettoyage intelligent :** Suppression synchronisée (lorsqu'une image est supprimée de la base de données, le fichier physique `.png`/`.jpg` est également effacé du disque dur pour éviter les fuites de stockage).

### ⚡ Communication Temps Réel
* **WebSockets & STOMP :** Serveur WebSocket intégré pour diffuser des événements en direct aux clients connectés.
* **Canaux ciblés :** Notifications de rafraîchissement envoyées sur des "topics" spécifiques (ex: `/topic/groupe/{id}` pour les membres, `/topic/membre/{id}` pour les images personnelles) afin d'éviter les requêtes API inutiles.

### 🔐 Sécurité & Authentification
* **Spring Security & JWT :** Sécurisation complète des routes de l'API avec des JSON Web Tokens (Bearer Token).
* **Contrôle d'accès :** Vérification des permissions selon les rôles (Admin/Invité) directement côté serveur.
* **CORS Configuré :** Autorisation des requêtes cross-origin pour communiquer fluidement avec le Front-End React.



## 🛠️ Technologies & Stack Technique

* **Framework :** Java 17+ / Spring Boot 3
* **Sécurité :** Spring Security, JWT (io.jsonwebtoken)
* **Base de Données :** H2 Database Engine, Spring Data JPA, Hibernate
* **WebSockets :** Spring Boot Starter WebSocket
* **Build Tool :** Maven (ou Gradle)


## 🚀 Installation et Lancement (Local)

### 1. Prérequis
* [Java JDK 17](https://adoptium.net/) (ou version supérieure).
* [Maven](https://maven.apache.org/) (si non inclus dans votre IDE).

### 2. Cloner le projet

git clone [https://github.com/AlexisCerda/dashboard-backend.git](https://github.com/AlexisCerda/dashboard-backend.git)
cd dashboard-backend
3. Configuration requise (application.properties)
Assurez-vous que votre fichier src/main/resources/application.properties contient les limites de taille nécessaires pour l'upload de fichiers :

Properties
# Autoriser les uploads jusqu'à 10 Mo
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Configuration de la base H2 (Exemple)
spring.datasource.url=jdbc:h2:file:./data/dashboard_db;AUTO_SERVER=TRUE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
4. Lancer le serveur
Depuis votre terminal à la racine du projet :


./mvnw spring-boot:run
L'API sera accessible sur http://localhost:8080.

🗃️ Accès à la base de données (H2 Console)
En environnement de développement, vous pouvez visualiser et modifier les tables en direct via l'interface web intégrée :

Allez sur http://localhost:8080/h2-console

URL JDBC : Entrez l'URL exacte définie dans votre application.properties (ex: jdbc:h2:file:./data/dashboard_db).

Utilisateur : sa (mot de passe vide par défaut).

(Note : La sécurité Spring Security a été configurée pour autoriser les iframes et désactiver le CSRF spécifiquement sur cette route).

👨‍💻 Auteur
Développé par Alexis Cerda De Almeida Vilaca dans le cadre des projets de la Préfecture (SIDSIC).