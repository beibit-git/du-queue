package kz.dulaty.queue.feature.ticket.service;

import kz.dulaty.queue.feature.ticket.data.dto.TicketDto;

import java.util.List;

public interface GetTicketService {
    List<TicketDto> getActiveTickets();
}
