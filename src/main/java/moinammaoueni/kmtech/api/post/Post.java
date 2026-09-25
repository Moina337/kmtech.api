package moinammaoueni.kmtech.api.post;

import jakarta.persistence.*;
import lombok.*;

import moinammaoueni.kmtech.api.organization.Organization;
import moinammaoueni.kmtech.api.user.User;

import java.time.LocalDateTime;


@Entity
@Table(name = "posts", uniqueConstraints = {
    @UniqueConstraint(columnNames = "slug")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * Profil utilisateur utilisé comme contexte de publication.
     * Peut être null si post d'organisation.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Organisation utilisée comme contexte de publication.
     * Peut être null si post personnel.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}