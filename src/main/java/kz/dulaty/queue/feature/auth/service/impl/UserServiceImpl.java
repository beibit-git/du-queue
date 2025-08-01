package kz.dulaty.queue.feature.auth.service.impl;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import kz.dulaty.queue.core.exception.CustomAuthenticationException;
import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.core.exception.UserAlreadyExistsException;
import kz.dulaty.queue.core.properties.EmailProperties;
import kz.dulaty.queue.feature.auth.data.dto.ResetPasswordRequest;
import kz.dulaty.queue.feature.auth.data.dto.SignInRequestDto;
import kz.dulaty.queue.feature.auth.data.dto.SignUpRequest;
import kz.dulaty.queue.feature.auth.data.dto.UserInfoDto;
import kz.dulaty.queue.feature.auth.data.entity.ConfirmationToken;
import kz.dulaty.queue.feature.auth.data.entity.User;
import kz.dulaty.queue.feature.auth.data.enums.ConfirmationTokenType;
import kz.dulaty.queue.feature.auth.data.enums.SafetyRole;
import kz.dulaty.queue.feature.auth.data.mapper.UserInfoMapper;
import kz.dulaty.queue.feature.auth.service.ConfirmationTokenService;
import kz.dulaty.queue.feature.auth.service.EmailService;
import kz.dulaty.queue.feature.auth.service.RoleService;
import kz.dulaty.queue.feature.auth.service.UserService;
import kz.dulaty.queue.feature.auth.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final PasswordEncoder encoder;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final EmailProperties emailProperties;
    private final EmailService emailService;

    @Override
    public UserInfoDto signIn(SignInRequestDto requestDto, HttpServletRequest request, HttpServletResponse response) throws NotFoundException, CustomAuthenticationException {
        User user = userRepository.findByEmail(requestDto.email()).orElseThrow(() -> new NotFoundException("User not found"));
        if (!user.isActive()) {
            throw new CustomAuthenticationException("Пользователь не активирован!");
        }
        var token = UsernamePasswordAuthenticationToken.unauthenticated(
                requestDto.email(), requestDto.password());
        authenticate(token, request, response);
        log.info("User {} has been authenticated", requestDto.email());
        return getUserInfo(requestDto.email());
    }

    @Override
    @Transactional
    public void signUp(SignUpRequest requestDto) throws UserAlreadyExistsException {

        checkIfUserExistsByPhoneNumber(requestDto.phoneNumber());
        checkIfUserExistsByEmail(requestDto.email());

        User user = UserInfoMapper.USER_INFO_MAPPER.toEntity(requestDto);

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(roleService.findByType(SafetyRole.GUEST)));
        userRepository.save(user);

        user.setActive(true);

        //sendVerificationToken(user);
        log.info("User {} registered", requestDto.email());
    }

    @Override
    public void confirmEmail(String token, HttpServletRequest request, HttpServletResponse response) throws AuthException {
        var confirmationToken = validateToken(token, ConfirmationTokenType.EMAIL_CONFIRMATION);

        var user = confirmationToken.getUser();
        user.setActive(true);
        userRepository.save(user);

        confirmationTokenService.deleteByToken(confirmationToken);
    }

    @Override
    public void forgotPassword(String email) throws NotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));

        var token = confirmationTokenService.generateAndSaveToken(user, ConfirmationTokenType.PASSWORD_RESET);

        String resetPasswordLink = emailProperties.getResetPasswordLinkPattern().formatted(token);

        emailService.sendEmail(
                user.getEmail(),
                "Reset Password",
                "reset-password.ftl",
                Map.of("name", user.getName(), "resetPasswordLink", resetPasswordLink));
    }

    @Override
    public void resetPassword(String token, ResetPasswordRequest resetPasswordDto) throws AuthException {
        if (!resetPasswordDto.confirmPassword().equals(resetPasswordDto.password())) {
            throw new AuthException("Passwords do not match");
        }

        var confirmationToken = validateToken(token, ConfirmationTokenType.PASSWORD_RESET);

        var user = confirmationToken.getUser();
        user.setPassword(encoder.encode(resetPasswordDto.password()));
        userRepository.save(user);

        confirmationTokenService.deleteByToken(confirmationToken);
    }

    @Override
    public UserInfoDto getUserInfo(String email) throws NotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        return UserInfoMapper.USER_INFO_MAPPER.toDto(user);
    }

    @Override
    public void signOut(HttpServletRequest request, HttpServletResponse response) {
        // Получаем текущий контекст безопасности
        var context = securityContextHolderStrategy.getContext();

        // Проверяем, что пользователь аутентифицирован
        if (context.getAuthentication() != null) {
            // Устанавливаем контекст аутентификации в null для завершения сеанса
            securityContextHolderStrategy.clearContext();

            // Удаляем контекст безопасности из хранилища
            securityContextRepository.saveContext(securityContextHolderStrategy.createEmptyContext(), request, response);

            log.info("Пользователь вышел из системы");
        } else {
            log.warn("Пользователь не аутентифицирован");
        }
    }

    private void authenticate(UsernamePasswordAuthenticationToken token, HttpServletRequest request, HttpServletResponse response) {
        var authentication = authenticationManager.authenticate(token);
        var context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }

    private void checkIfUserExistsByPhoneNumber(String phoneNumber) throws UserAlreadyExistsException {
        if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с таким номером телефона уже существует");
        }
    }

    private void checkIfUserExistsByEmail(String email) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с таким адресом электронной почты уже существует");
        }
    }

    private ConfirmationToken validateToken(String token, ConfirmationTokenType tokenType) throws AuthException {
        var confirmationToken = confirmationTokenService.findByToken(token);

        var isTokenExpired = confirmationToken.getExpirationDate().isBefore(LocalDateTime.now());
        if (isTokenExpired) {
            throw new AuthException("Token expired");
        }

        if (!confirmationToken.getConfirmationTokenType().equals(tokenType)) {
            throw new AuthException("Invalid token type");
        }

        return confirmationToken;
    }

    private void sendVerificationToken(User user) {
        var token = confirmationTokenService.generateAndSaveToken(user, ConfirmationTokenType.EMAIL_CONFIRMATION);
        String confirmationLink = emailProperties.getEmailConfirmationLinkPattern().formatted(token);

        emailService.sendEmail(
                user.getEmail(),
                "Email Confirmation",
                "confirmation-mail.ftl",
                Map.of("name", user.getName(), "confirmationLink", confirmationLink));
    }
}
