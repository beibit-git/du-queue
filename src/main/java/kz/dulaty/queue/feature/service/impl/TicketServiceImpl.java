package kz.dulaty.queue.feature.service.impl;

import kz.dulaty.queue.feature.data.dto.TicketDto;
import kz.dulaty.queue.feature.data.dto.TicketRequestDto;
import kz.dulaty.queue.feature.data.repository.TicketRepository;
import kz.dulaty.queue.feature.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private TicketRepository ticketRepository;

    @Override
    public TicketDto generateTicket(TicketRequestDto request) {
        return null;
    }
}
