package kz.dulaty.queue.feature.auth.service;

import kz.dulaty.queue.feature.auth.data.entity.ConfirmationToken;
import kz.dulaty.queue.feature.auth.data.entity.User;
import kz.dulaty.queue.feature.auth.data.enums.ConfirmationTokenType;

public interface ConfirmationTokenService {
    String generateAndSaveToken(User user, ConfirmationTokenType confirmationTokenType);

    ConfirmationToken findByToken(String token);

    void deleteByToken(ConfirmationToken token);

}