# KmTech API

API backend de KmTech, une plateforme pour l'écosystème tech comorien : profils développeurs, organisations, projets et publications.

## Stack technique

- Java 21, Spring Boot 3.5
- Spring Security (JWT, stateless)
- Spring Data JPA + PostgreSQL
- Brevo (SMTP) pour les emails transactionnels
- springdoc-openapi (Swagger UI)
- Maven

## Fonctionnalités

- **Authentification** : inscription, connexion, vérification d'email par lien à usage unique
- **Profils utilisateurs** : profil développeur avec compétences, portfolio, localisation
- **Organisations** : création, gestion des membres (owner / admin / membre), workflow de validation par un administrateur de la plateforme (`PENDING` → `ACTIVE` / `REJECTED`)
- **Projets et publications** : rattachés à un utilisateur ou à une organisation, avec commentaires
- **Espace admin** : validation/refus des organisations, gestion du statut et du rôle des utilisateurs

## Architecture de l'API

Les contrôleurs sont séparés selon leur niveau d'exposition, avec des préfixes cohérents :

| Préfixe | Accès | Exemple |
|---|---|---|
| `/api/public/**` | Public, sans authentification | `/api/public/projects` |
| `/api/{ressource}/**` | Authentifié, gestion de ses propres ressources | `/api/projects/me` |
| `/api/admin/**` | Réservé au rôle `ADMIN` | `/api/admin/organizations/pending` |

Chaque route publique et chaque route de gestion appellent le même service applicatif — pas de duplication de la logique métier entre les deux.

## Démarrage avec Docker

### Prérequis

- Docker et Docker Compose
- Un compte [Brevo](https://www.brevo.com) (offre gratuite) pour l'envoi d'emails

### Configuration

```bash
cp .env.example .env
```

Renseigner dans `.env` :
- `DB_PASSWORD` : un mot de passe pour la base locale
- `JWT_SECRET_KEY` : une chaîne aléatoire longue (ne jamais réutiliser une valeur d'exemple)
- `BREVO_SMTP_LOGIN` / `BREVO_SMTP_KEY` : identifiants SMTP depuis le tableau de bord Brevo (section *SMTP & API*)
- `MAIL_FROM` : une adresse expéditrice vérifiée dans Brevo
- `ADMIN_NOTIFICATION_EMAIL` : l'adresse recevant les notifications d'organisation en attente

### Lancer

```bash
docker compose up --build
```

L'API est disponible sur `http://localhost:8080`.
La documentation Swagger sur `http://localhost:8080/swagger-ui.html`.

### Arrêter

```bash
docker compose down
```

Ajouter `-v` pour supprimer aussi les volumes (base de données et fichiers uploadés) :

```bash
docker compose down -v
```

## Développement local sans Docker

```bash
mvn spring-boot:run
```

Nécessite une instance PostgreSQL locale et les variables d'environnement de `application.yml` définies (voir la configuration de lancement de ton IDE).

## Variables d'environnement

| Variable | Description | Requise |
|---|---|---|
| `DB_HOST`, `DB_PORT` | Hôte et port de PostgreSQL | Non (valeurs par défaut locales) |
| `DB_USERNAME`, `DB_PASSWORD` | Identifiants de la base | Oui |
| `JWT_SECRET_KEY` | Clé de signature des tokens JWT | Oui |
| `HIBERNATE_DDL_MODE` | `update` en local, `validate` recommandé en production | Non |
| `BREVO_SMTP_LOGIN`, `BREVO_SMTP_KEY` | Identifiants SMTP Brevo | Oui |
| `MAIL_FROM` | Adresse expéditrice des emails | Oui |
| `ADMIN_NOTIFICATION_EMAIL` | Adresse recevant les notifications admin | Oui |
| `FRONTEND_URL` | Utilisée pour construire les liens de vérification d'email | Oui |

## Sécurité

- Mots de passe hashés avec BCrypt
- Tokens JWT stateless, rôle relu depuis la base à chaque requête (une suspension ou un changement de rôle prend effet immédiatement, sans attendre l'expiration du token)
- Tokens de vérification d'email stockés sous forme de hash SHA-256, jamais en clair, valables 24h et à usage unique
- Endpoints `/api/admin/**` restreints au rôle `ADMIN`

## Statut du projet

MVP en cours de construction. Hors périmètre pour l'instant : offres d'emploi, notifications in-app, likes sur les publications, multi-administrateurs.

## Licence

À définir.
