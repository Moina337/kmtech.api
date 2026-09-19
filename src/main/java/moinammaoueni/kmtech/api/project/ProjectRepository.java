package moinammaoueni.kmtech.api.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import moinammaoueni.kmtech.api.organization.Organization;
import moinammaoueni.kmtech.api.user.User;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Recherche un projet à partir de son slug.
     *
     * Utilisé notamment pour les opérations nécessitant
     * l'identification publique d'un projet.
     */
    Optional<Project> findBySlug(String slug);

    /**
     * Recherche un projet public à partir de son slug.
     *
     * Permet de récupérer uniquement les projets ayant
     * le statut demandé, par exemple PUBLISHED.
     */
    Optional<Project> findBySlugAndStatus(
            String slug,
            ProjectStatus status
    );

    /**
     * Récupère les projets d'un utilisateur avec un statut donné.
     *
     * Exemple :
     * - projets personnels publiés
     * - projets personnels en brouillon
     */
    List<Project> findByUserAndStatus(
            User user,
            ProjectStatus status
    );

    /**
     * Récupère les projets d'une organisation avec un statut donné.
     *
     * Utilisé notamment pour récupérer les projets publics
     * d'une organisation.
     */
    List<Project> findByOrganizationAndStatus(
            Organization organization,
            ProjectStatus status
    );

    /**
     * Récupère tous les projets appartenant à une organisation.
     *
     * Contrairement à findByOrganizationAndStatus(),
     * cette méthode ne filtre pas sur le statut.
     *
     * Elle est donc utile pour l'espace de gestion de l'organisation,
     * où les projets DRAFT et PUBLISHED doivent être visibles.
     */
    List<Project> findByOrganization(
            Organization organization
    );

    /**
     * Récupère tous les projets ayant un statut donné.
     *
     * Exemple :
     * récupérer tous les projets PUBLISHED pour l'affichage public.
     */
    List<Project> findByStatus(
            ProjectStatus status
    );

    /**
     * Récupère tous les projets personnels d'un utilisateur.
     *
     * Les projets d'organisation ne sont pas concernés.
     */
    List<Project> findByUser(
            User user
    );

    /**
     * Recherche un projet personnel appartenant à un utilisateur précis.
     *
     * Cette méthode est particulièrement importante pour les opérations
     * de gestion d'un projet personnel.
     *
     * Le projet doit appartenir à l'utilisateur fourni.
     * Cela évite de récupérer un projet appartenant à un autre utilisateur.
     */
    Optional<Project> findByIdAndUser(
            Long projectId,
            User user
    );
    
    
    Optional<Project> findByIdAndOrganization(
            Long projectId,
            Organization organization
    );

    /**
     * Vérifie si un slug est déjà utilisé par un projet.
     *
     * Utilisé lors de la génération d'un nouveau slug
     * afin d'éviter les doublons.
     */
    boolean existsBySlug(String slug);
}