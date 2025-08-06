package kz.dulaty.queue.feature.ticket.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.ticket.data.dto.TicketDto;
import kz.dulaty.queue.feature.ticket.data.dto.TicketRequestDto;
import kz.dulaty.queue.feature.ticket.service.GetTicketService;
import kz.dulaty.queue.feature.ticket.service.TicketCallingService;
import kz.dulaty.queue.feature.ticket.service.TicketGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "Генерация талона")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/queue/ticket")
public class TicketController {
    private final TicketGenerationService ticketGenerationService;
    private final TicketCallingService ticketCallingService;
    private final GetTicketService getTicketService;

    @PostMapping("/generate")
    public ResponseEntity<TicketDto> generateTicket(@RequestBody TicketRequestDto request) {
        TicketDto ticket = ticketGenerationService.generateTicket(request);
        return ResponseEntity.ok(ticket);
    }

    @PostMapping("/call-next")
    @PreAuthorize("hasAuthority('MANAGER')")
    public String callNextTicket(Principal principal) throws NotFoundException {
        return ticketCallingService.callNextTicket(principal.getName());
    }

    @GetMapping("/active-tickets")
    public List<TicketDto> getActiveTickets() {
        return getTicketService.getActiveTickets();
    }
}
