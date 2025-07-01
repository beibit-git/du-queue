package kz.dulaty.queue.feature.service;

import kz.dulaty.queue.feature.data.dto.TicketDto;
import kz.dulaty.queue.feature.data.dto.TicketRequestDto;

public interface TicketService {
    TicketDto generateTicket(TicketRequestDto request);
}
