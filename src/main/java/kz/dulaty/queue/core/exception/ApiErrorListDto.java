package kz.dulaty.queue.core.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.Map;

@Setter
@Getter
@Schema(description = "Ошибка")
public class ApiErrorListDto {

    @Schema(description = "Время ошибки")
    private ZonedDateTime timestamp;

    @Schema(description = "Http статус ошибки")
    private HttpStatus status;

    @Schema(description = "Код ошибки")
    private String errorCode;

    @Schema(description = "Сообщение ошибки")
    private Map<String, String> error;

    @Schema(description = "Ресурс")
    private String path;

    public ApiErrorListDto(ZonedDateTime timestamp, HttpStatus status, String errorCode, Map<String, String> error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.errorCode = errorCode;
        this.path = path;
    }
}
