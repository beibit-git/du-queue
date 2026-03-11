package kz.dulaty.queue.feature.tts;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Голосовые объявления")
@RestController
@RequestMapping("/api/v1/queue/tts")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('MANAGER')")
public class TtsController {

    private final TtsService ttsService;

    /**
     * Генерирует MP3-объявление вызова талона.
     * Пример: GET /api/v1/queue/tts/announce?ticketNumber=A002&windowNumber=2&lang=ru
     */
    @GetMapping(value = "/announce", produces = "audio/mpeg")
    public ResponseEntity<byte[]> announce(
            @RequestParam String ticketNumber,
            @RequestParam String windowNumber,
            @RequestParam(defaultValue = "ru") String lang
    ) {
        byte[] audio = ttsService.announce(ticketNumber, windowNumber, lang);

        if (audio.length == 0) {
            return ResponseEntity.noContent().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/mpeg"));
        headers.setContentLength(audio.length);
        // Кешируем в браузере — один и тот же талон/окно не скачивать повторно
        headers.setCacheControl("public, max-age=3600");

        return ResponseEntity.ok().headers(headers).body(audio);
    }
}
