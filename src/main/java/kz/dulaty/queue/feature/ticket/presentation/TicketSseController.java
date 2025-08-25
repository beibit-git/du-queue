package kz.dulaty.queue.feature.ticket.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import kz.dulaty.queue.feature.ticket.data.dto.TicketDto;
import kz.dulaty.queue.feature.ticket.service.sseEmitter.SseEmitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "Генерация талона")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/queue/ticket")
public class TicketSseController {
    private final SseEmitters emitters;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        return emitters.addEmitter();
    }

    /** Этот метод можно вызвать из сервиса, либо удалить и слать из сервиса напрямую */
    public void onTicketCalled(TicketDto ticket) {
        emitters.sendToAll(ticket);
    }
}
