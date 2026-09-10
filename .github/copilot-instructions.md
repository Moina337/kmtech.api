# KMTech API — Copilot Instructions

## 1. Projet

KMTech est une plateforme web destinée à rendre visibles et connectés les acteurs de l'écosystème technologique comorien.

Objectif MVP :

* découvrir des personnes et leurs compétences ;
* découvrir des organisations ;
* découvrir des projets ;
* découvrir des applications ;
* découvrir des opportunités ;
* permettre aux utilisateurs de contribuer au contenu.

Le MVP doit rester simple, maintenable et rapide à développer.

---

## 2. Stack technique

* Java 21
* Spring Boot 3.5.16
* Maven
* PostgreSQL
* Spring Web
* Spring Data JPA
* Spring Security
* Lombok
* Jakarta Validation
* MapStruct
 lombok-mapstruct-binding
 JUnit / Spring Boot Test

ORM :

* Hibernate / Spring Data JPA

API :

* REST

Architecture :

* Modular Monolith

---

## 3. Architecture des packages

Organiser le backend par module métier.

Modules :

* auth
* user
* skill
* organization
* project
* application
* opportunity
* common
* config

Ne pas utiliser une architecture globale :

```text
controller/
service/
repository/
entity/
```

Chaque module contient ses propres classes :

```text
module/
├── Entity.java
├── Repository.java
├── Service.java
├── Controller.java
 mapper
└── dto/
```

`common` contient uniquement les éléments réellement partagés.

`config` contient les configurations techniques.

---

## 4. Modèle métier MVP

Entités principales :

* User
* Skill
* UserSkill
* Organization
* OrganizationMember
* Project
* Application
* Opportunity

Ne pas créer automatiquement :

* Profile
* Innovation
* Claim
* Verification
* Follow
* Favorite
* Message
* Notification
* Feed
* ProjectSkill
* OrganizationSkill
* Candidate
* ApplicationSubmission

Une nouvelle entité doit répondre à un besoin métier explicite.

---

## 5. Relations principales

```text
USER N:N SKILL
via USER_SKILL

USER N:N ORGANIZATION
via ORGANIZATION_MEMBER

USER 1:N PROJECT
ORGANIZATION 0..1:N PROJECT

USER 1:N APPLICATION
ORGANIZATION 0..1:N APPLICATION
PROJECT 0..1:N APPLICATION

USER 1:N OPPORTUNITY
ORGANIZATION 0..1:N OPPORTUNITY
```

Les relations doivent respecter le modèle métier.

Ne pas ajouter de relation JPA simplement pour faciliter une requête.

---

## 6. Créateur et organisation

Pour Project, Application et Opportunity :

`createdBy` représente l'utilisateur qui crée ou soumet le contenu.

`organization` représente l'organisation réellement liée au contenu.

Ces deux notions sont différentes.

Une organisation est optionnelle pour un projet, une application ou une opportunité.

---

## 7. Sécurité

Rôles globaux :

```text
ROLE_USER
ROLE_ADMIN
```

Rôles d'organisation :

```text
OWNER
ADMIN
MEMBER
```

Ne pas créer de rôles globaux tels que :

```text
ROLE_DEVELOPER
ROLE_COMPANY
ROLE_STUDENT
ROLE_STARTUP
```

Les permissions métier doivent être vérifiées dans les services.

Les controllers ne doivent pas contenir toute la logique métier.

---

## 8. Règles de développement

* Utiliser les DTO pour l'API.
* Ne jamais exposer directement les entités JPA dans les réponses REST lorsque cela peut exposer des données internes.
* Ne jamais exposer le mot de passe.
* Utiliser les validations Jakarta lorsque nécessaire.
* Utiliser `@Transactional` uniquement lorsque la transaction est réellement nécessaire.
* Garder les controllers minces.
* Garder la logique métier dans les services.
* Garder les repositories centrés sur l'accès aux données.
* Éviter les dépendances circulaires entre modules.
* Ne pas ajouter de dépendance Maven sans nécessité.
* Ne pas refactoriser du code non concerné par la tâche.

---

## 9. Règles pour Copilot

Copilot doit implémenter les décisions existantes et ne doit pas inventer l'architecture.

Avant de créer une classe, une relation, une table ou une dépendance, vérifier qu'elle correspond au modèle du projet.

Si une information nécessaire n'est pas définie :

* ne pas l'inventer ;
* signaler ce qui manque.

Pour chaque tâche :

1. modifier uniquement les fichiers nécessaires ;
2. ne pas créer de fonctionnalité supplémentaire ;
3. ne pas modifier les autres modules sans nécessité ;
4. ne pas ajouter de dépendance sans justification ;
5. conserver les conventions existantes ;
6. produire le minimum de code nécessaire.

Travailler fonctionnalité par fonctionnalité.

Ne pas générer toute l'application en une seule fois.

---

## 10. Ordre de développement du MVP

```text
1. Structure des modules
2. User
3. Auth
4. Skill
5. UserSkill
6. Organization
7. OrganizationMember
8. Project
9. Application
10. Opportunity
11. Recherche / découverte
12. Administration / modération
13. Tests
14. Déploiement
```

Ne pas avancer à l'étape suivante tant que l'étape actuelle n'est pas fonctionnelle.

---

## 11. Principe général

Priorité :

```text
Simple
→ Compréhensible
→ Testable
→ Sécurisé
→ Maintenable
→ Évolutif
```

Ne pas sur-architecturer le MVP.
