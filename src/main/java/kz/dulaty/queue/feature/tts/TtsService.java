package kz.dulaty.queue.feature.tts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class TtsService {

    private static final String YANDEX_TTS_URL =
            "https://tts.api.cloud.yandex.net/speech/v1/tts:synthesize";

    @Value("${app.tts.yandex-api-key:}")
    private String apiKey;

    @Value("${app.tts.yandex-folder-id:}")
    private String folderId;

    // Кеш: ключ = "lang|text", значение = MP3-байты
    private final Map<String, byte[]> cache = new ConcurrentHashMap<>();

    private final RestTemplate restTemplate = new RestTemplate();

    public byte[] announce(String ticketNumber, String windowNumber, String lang) {
        String text = buildText(ticketNumber, windowNumber, lang);
        return cache.computeIfAbsent(lang + "|" + text, k -> synthesize(text, lang));
    }

    private String buildText(String ticketNumber, String windowNumber, String lang) {
        if ("kk".equals(lang)) {
            return ticketNumber + " нөмірлі талон иесі, " + windowNumber + " терезеге өтіңіз";
        }
        return "Абитуриент с номером талона " + ticketNumber + ", пройдите к окну " + windowNumber;
    }

    private byte[] synthesize(String text, String lang) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("TTS: yandex-api-key не задан, пропуск синтеза");
            return new byte[0];
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Api-Key " + apiKey);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("text", text);
            body.add("lang", "kk".equals(lang) ? "kk-KZ" : "ru-RU");
            body.add("voice", "kk".equals(lang) ? "amira" : "alena");
            body.add("format", "mp3");
            body.add("speed", "0.9");
            if (folderId != null && !folderId.isBlank()) {
                body.add("folderId", folderId);
            }

            ResponseEntity<byte[]> response = restTemplate.postForEntity(
                    YANDEX_TTS_URL,
                    new HttpEntity<>(body, headers),
                    byte[].class
            );

            byte[] audio = response.getBody();
            return audio != null ? audio : new byte[0];
        } catch (Exception e) {
            log.error("TTS: ошибка Yandex SpeechKit — {}", e.getMessage());
            return new byte[0];
        }
    }
}
