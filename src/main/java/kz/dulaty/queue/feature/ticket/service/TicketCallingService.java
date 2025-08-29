package kz.dulaty.queue.feature.ticket.service;

import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.ticket.data.dto.TicketDto;

import java.util.List;

public interface TicketCallingService {
    /**
     * Calls the next ticket in the queue.
     *
     * @return The ticket number of the called ticket.
     */
    TicketDto callNextTicket(String email) throws NotFoundException;
}
