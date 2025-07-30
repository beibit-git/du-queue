package kz.dulaty.queue.core.logging.aspect;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Aspect
@Slf4j
@NoArgsConstructor
public class LoggingAspect {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String EMPTY_STRING = "";
    public static final String EMPTY_COOKIE = "[]";
    public static final String COMMA = ",";
    private List<Consumer<String>> logConsumers = new ArrayList<>();
//    private Function<JoinPoint, String> usernameExtractor =
//            j -> extractAuthenticationUsername()
//                    .orElseGet(() -> extractShepSenderUsername(j).orElse("anonymous"));
//
//
//    private Optional<String> extractAuthenticationUsername() {
//        var securityContext = SecurityContextHolder.getContext();
//        if (securityContext.getAuthentication() != null
//                && securityContext.getAuthentication().getPrincipal() instanceof EhUserPrincipal) {
//            return Optional.of(((EhUserPrincipal) securityContext.getAuthentication().getPrincipal()).getUsername());
//        }
//
//        return Optional.empty();
//    }

}
