package moinammaoueni.kmtech.api.organization;

public enum OrganizationStatus {
    PENDING,    // créée, en attente de validation admin — nouveau
    ACTIVE,     // validée par l'admin, visible publiquement
    REJECTED,   // refusée par l'admin — nouveau
    INACTIVE    // désactivée par l'owner (déjà existant, inchangé)
}