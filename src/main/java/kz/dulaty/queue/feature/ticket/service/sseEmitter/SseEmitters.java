package kz.dulaty.queue.feature.ticket.service.sseEmitter;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SseEmitters {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    /**
     * Регистрируем нового подписчика
     */
    public SseEmitter addEmitter() {
        // 0L = без таймаута; можно поставить, например, 30мин: new SseEmitter(30 * 60_000L)
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((ex) -> emitters.remove(emitter));

        // можно отправить "привет" чтобы сразу открыть поток
        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("ok", MediaType.TEXT_PLAIN));
        } catch (IOException ignored) {
        }

        return emitter;
    }

    /**
     * Рассылаем событие всем активным подписчикам
     */
    public void sendToAll(Object payload) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("ticket-called")
                        .data(payload, MediaType.APPLICATION_JSON));
            } catch (IOException ex) {
                // клиент отключился – убрать из списка
                emitters.remove(emitter);
            }
        }
    }
}
