package kz.dulaty.queue.feature.ticket.service.impl;

import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.manager.data.entity.Manager;
import kz.dulaty.queue.feature.manager.data.repository.ManagerRepository;
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
    private final ManagerRepository managerRepository;

    @Override
    public List<TicketDto> getActiveTickets() {
        List<Ticket> ticketList = ticketRepository.findAllActiveTickets();
        return ticketList.stream()
                .map(TicketMapper.TICKET_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDto> managerCurrentTickets(String email) throws NotFoundException {
        Manager manager = managerRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("Manager not found with email: " + email));

        Long managerId = manager.getId();

        List<Ticket> tickets = ticketRepository.getLast5TicketByManagerId(
                managerId
        );
        return tickets.stream().map(TicketMapper.TICKET_MAPPER::toDto).toList();
    }
}
