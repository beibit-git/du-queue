package kz.dulaty.queue.feature.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.dulaty.queue.feature.ticket.data.dto.WsEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

    private final ObjectMapper objectMapper;

    // Потокобезопасный список подключённых клиентов
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        // Timeout = 0 означает бесконечное соединение
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
                emitter.send(SseEmitter.event()
                        .name("queue-event")
                        .data(json));
            } catch (Exception e) {
                emitters.remove(emitter);
            }
        }

        log.debug("SSE broadcast {} to {} clients", event.eventType(), emitters.size());
    }

    public int getConnectedCount() {
        return emitters.size();
    }
}
