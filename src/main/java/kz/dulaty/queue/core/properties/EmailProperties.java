package kz.dulaty.queue.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties("dulaty.mail-properties")
public class EmailProperties {
    private String domain;
    private String emailConfirmationLinkPattern;
    private String resetPasswordLinkPattern;
}
