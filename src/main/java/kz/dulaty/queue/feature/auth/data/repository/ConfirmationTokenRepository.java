package kz.dulaty.queue.feature.auth.data.repository;

import kz.dulaty.queue.feature.auth.data.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    @Modifying
    @Query("SELECT ct FROM ConfirmationToken ct WHERE ct.expirationDate <= ?1")
    void deleteAllByExpirationDateBefore(LocalDateTime now);
}
