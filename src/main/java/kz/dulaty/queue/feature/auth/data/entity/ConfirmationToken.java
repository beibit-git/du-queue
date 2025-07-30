package kz.dulaty.queue.feature.auth.data.entity;

import jakarta.persistence.*;
import kz.dulaty.queue.feature.auth.data.enums.ConfirmationTokenType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "confirmation_tokens")
public class ConfirmationToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ConfirmationTokenType confirmationTokenType;

    private String token;

    private LocalDateTime expirationDate;

    @OneToOne
    private User user;
}