package kz.dulaty.queue.feature.ticket.service;

import kz.dulaty.queue.feature.ticket.data.dto.TicketDto;
import kz.dulaty.queue.feature.ticket.data.dto.TicketRequestDto;

public interface TicketGenerationService {
    TicketDto generateTicket(TicketRequestDto request);
}
