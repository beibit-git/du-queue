package kz.dulaty.queue.feature.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import kz.dulaty.queue.core.exception.CustomAuthenticationException;
import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.core.exception.UserAlreadyExistsException;
import kz.dulaty.queue.feature.auth.data.dto.ResetPasswordRequest;
import kz.dulaty.queue.feature.auth.data.dto.SignInRequestDto;
import kz.dulaty.queue.feature.auth.data.dto.SignUpRequest;
import kz.dulaty.queue.feature.auth.data.dto.UserInfoDto;
import kz.dulaty.queue.feature.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "Аутентификация и авторизация пользователей")
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/sign-up")
    @Operation(summary = "Регистрация нового пользователя")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewUser(@RequestBody @Valid SignUpRequest request) throws NotFoundException, UserAlreadyExistsException {
        service.signUp(request);
    }

    @GetMapping("/user-info")
    @PreAuthorize("hasAnyAuthority('GUEST', 'ADMIN', 'MODERATOR', 'MANAGER')")
    public UserInfoDto userProfile(Principal principal) throws NotFoundException {
        return service.getUserInfo(principal.getName());
    }

    @PostMapping("/sign-in")
    @Operation(summary = "Вход с помощью логина и пароля")
    public UserInfoDto authenticateAndGetToken(@RequestBody @Valid SignInRequestDto requestBody,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws NotFoundException, CustomAuthenticationException {
        log.info("Запрос на аутентификацию пользователя {}", requestBody.email());

        UserInfoDto userInfoDto = service.signIn(requestBody, request, response);
        log.info("Пользователь {} успешно аутентифицирован", requestBody.email());
        return userInfoDto;
    }

    @PostMapping("/sign-out")
    @Operation(summary = "Выход из аккаунта")
    @ResponseStatus(HttpStatus.OK)
    public void signOut(HttpServletRequest request, HttpServletResponse response) {
        log.info("Запрос на выход из аккаунта");
        service.signOut(request, response);
        log.info("Пользователь успешно вышел из аккаунта");
    }

    @GetMapping("/confirm-email")
    @Operation(summary = "Подтверждение email")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void login(HttpServletRequest request,
               HttpServletResponse response,
               @RequestParam(name = "token") @NotBlank String token) throws AuthException {
        service.confirmEmail(token, request, response);
    }

    @PostMapping("/forgot-password/{email}")
    @Operation(summary = "Восстановление пароля")
    @ResponseStatus(HttpStatus.OK)
    void forgotPassword(@PathVariable(name = "email") @NotBlank String email) throws NotFoundException {
        service.forgotPassword(email);
    }

    @PostMapping("/reset-password/{token}")
    @Operation(summary = "Сброс пароля")
    @ResponseStatus(HttpStatus.OK)
    void resetPassword(@PathVariable(name = "token") @NotBlank String token,
                       @RequestBody @Valid ResetPasswordRequest resetPasswordDto) throws AuthException {
        service.resetPassword(token, resetPasswordDto);
    }
}
