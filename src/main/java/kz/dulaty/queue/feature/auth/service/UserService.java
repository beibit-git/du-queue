package kz.dulaty.queue.feature.auth.service;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.dulaty.queue.core.exception.CustomAuthenticationException;
import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.core.exception.UserAlreadyExistsException;
import kz.dulaty.queue.feature.auth.data.dto.ResetPasswordRequest;
import kz.dulaty.queue.feature.auth.data.dto.SignInRequestDto;
import kz.dulaty.queue.feature.auth.data.dto.SignUpRequest;
import kz.dulaty.queue.feature.auth.data.dto.UserInfoDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserInfoDto signIn(SignInRequestDto requestDto, HttpServletRequest request, HttpServletResponse response) throws NotFoundException, CustomAuthenticationException;

    void signUp(SignUpRequest requestDto, MultipartFile file) throws NotFoundException, UserAlreadyExistsException;
    void confirmEmail(String token, HttpServletRequest request, HttpServletResponse response) throws AuthException;
    void forgotPassword(String email) throws NotFoundException;
    void resetPassword(String token, ResetPasswordRequest resetPasswordDto) throws AuthException;
    UserInfoDto getUserInfo(String email) throws NotFoundException;
    void signOut(HttpServletRequest request, HttpServletResponse response);
}
