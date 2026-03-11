package kz.dulaty.queue.feature.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import kz.dulaty.queue.feature.ticket.data.dto.WsEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

    private final ObjectMapper objectMapper;

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        log.debug("SSE client connected. Total: {}", emitters.size());
        return emitter;
    }

    public void broadcast(WsEventDto event) {
        if (emitters.isEmpty()) return;

        String json;
        try {
            json = objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            log.error("SSE: failed to serialize event", e);
            return;
        }

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("queue-event").data(json));
            } catch (Exception e) {
                emitters.remove(emitter);
            }
        }

        log.debug("SSE broadcast {} to {} clients", event.eventType(), emitters.size());
    }

    /**
     * Heartbeat каждые 30 секунд — не даёт Nginx закрыть соединение по таймауту.
     * SSE комментарий (: heartbeat) игнорируется браузером, но держит соединение живым.
     */
    @Scheduled(fixedDelay = 30_000)
    public void sendHeartbeat() {
        if (emitters.isEmpty()) return;
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().comment("heartbeat"));
            } catch (Exception e) {
                emitters.remove(emitter);
            }
        }
    }

    public int getConnectedCount() {
        return emitters.size();
    }
}
