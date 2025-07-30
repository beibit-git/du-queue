package kz.dulaty.queue.feature.auth.service.impl;

import kz.dulaty.queue.feature.auth.data.entity.ConfirmationToken;
import kz.dulaty.queue.feature.auth.data.entity.User;
import kz.dulaty.queue.feature.auth.data.enums.ConfirmationTokenType;
import kz.dulaty.queue.feature.auth.data.repository.ConfirmationTokenRepository;
import kz.dulaty.queue.feature.auth.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Transactional
    @Override
    public String generateAndSaveToken(User user, ConfirmationTokenType confirmationTokenType) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setUser(user);
        confirmationToken.setConfirmationTokenType(confirmationTokenType);
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(confirmationTokenType.getExpirationMinutes());
        confirmationToken.setExpirationDate(expirationDate);
        return confirmationTokenRepository.save(confirmationToken).getToken();
    }

    public ConfirmationToken findByToken(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));
    }

    @Override
    public void deleteByToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.delete(confirmationToken);
    }
}
