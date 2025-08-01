package kz.dulaty.queue.feature.ticket.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import kz.dulaty.queue.feature.ticket.data.dto.TicketDto;
import kz.dulaty.queue.feature.ticket.data.dto.TicketRequestDto;
import kz.dulaty.queue.feature.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Генерация талона")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/queue/ticket")
public class TicketController {
    private final TicketService ticketService;

    @PostMapping("/generate")
    public ResponseEntity<TicketDto> generateTicket(@RequestBody TicketRequestDto request) {
        TicketDto ticket = ticketService.generateTicket(request);
        return ResponseEntity.ok(ticket);
    }
}
