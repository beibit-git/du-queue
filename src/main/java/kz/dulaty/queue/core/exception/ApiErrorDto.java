package kz.dulaty.queue.core.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Setter
@Getter
@AllArgsConstructor
@Schema(description = "Ошибка")
public class ApiErrorDto {

    @Schema(description = "Время ошибки")
    private ZonedDateTime timestamp;

    @Schema(description = "Http статус ошибки")
    private HttpStatus status;

    @Schema(description = "Код ошибки")
    private String errorCode;

    @Schema(description = "Сообщение ошибки")
    private String message;

    @Schema(description = "Ресурс")
    private String path;

    public ApiErrorDto(HttpStatus status, String errorCode, String message, String path) {
        this.status = status;
        this.timestamp = ZonedDateTime.now();
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
    }
}
