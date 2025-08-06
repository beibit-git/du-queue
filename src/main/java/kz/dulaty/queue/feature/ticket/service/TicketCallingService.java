package kz.dulaty.queue.feature.ticket.service;

import kz.dulaty.queue.core.exception.NotFoundException;

public interface TicketCallingService {
    /**
     * Calls the next ticket in the queue.
     *
     * @return The ticket number of the called ticket.
     */
    String callNextTicket(String email) throws NotFoundException;
}
