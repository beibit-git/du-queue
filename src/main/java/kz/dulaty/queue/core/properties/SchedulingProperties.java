package kz.dulaty.queue.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "dulaty.scheduling")
public class SchedulingProperties {
    private String ticketCleaner;
}
