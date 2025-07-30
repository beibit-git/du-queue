package kz.dulaty.queue.core.logging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoggingRuntimeParameters {
    private String operation;
    private Map<String, String> additionalMessage = new HashMap<>();
    private String output;
}
