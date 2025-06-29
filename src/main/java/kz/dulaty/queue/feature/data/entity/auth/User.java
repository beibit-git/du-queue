package kz.dulaty.queue.feature.data.entity.auth;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -5957549204464639648L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isActive;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isLocked;

    @Column(nullable = false, updatable = false)
    private LocalDateTime lastLoginDateTime;
    private Integer passCount;
    private LocalDateTime passChangeDateTime;
}

