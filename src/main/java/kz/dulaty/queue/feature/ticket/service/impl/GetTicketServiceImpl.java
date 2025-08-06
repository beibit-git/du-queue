package kz.dulaty.queue.feature.ticket.service.impl;

import kz.dulaty.queue.feature.ticket.data.dto.TicketDto;
import kz.dulaty.queue.feature.ticket.data.entity.Ticket;
import kz.dulaty.queue.feature.ticket.data.mapper.TicketMapper;
import kz.dulaty.queue.feature.ticket.data.repository.TicketRepository;
import kz.dulaty.queue.feature.ticket.service.GetTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetTicketServiceImpl implements GetTicketService {
    private final TicketRepository ticketRepository;

    @Override
    public List<TicketDto> getActiveTickets() {
        List<Ticket> ticketList = ticketRepository.findAllActiveTickets();
        return ticketList.stream()
                .map(TicketMapper.TICKET_MAPPER::toDto)
                .collect(Collectors.toList());
    }
}
