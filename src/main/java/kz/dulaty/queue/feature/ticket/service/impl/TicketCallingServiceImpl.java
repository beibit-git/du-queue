package kz.dulaty.queue.feature.ticket.service.impl;

import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.manager.data.entity.Manager;
import kz.dulaty.queue.feature.manager.data.repository.ManagerRepository;
import kz.dulaty.queue.feature.ticket.data.dto.TicketDto;
import kz.dulaty.queue.feature.ticket.data.entity.Ticket;
import kz.dulaty.queue.feature.ticket.data.mapper.TicketMapper;
import kz.dulaty.queue.feature.ticket.data.repository.TicketRepository;
import kz.dulaty.queue.feature.ticket.service.TicketCallingService;
import kz.dulaty.queue.feature.ticket.service.sseEmitter.SseEmitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketCallingServiceImpl implements TicketCallingService {
    private final TicketRepository ticketRepository;
    private final ManagerRepository managerRepository;
    private final SseEmitters emitters;

    @Override
    @Transactional
    public String callNextTicket(String email) throws NotFoundException {
        Manager manager = managerRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("Manager not found with email: " + email));

        Long managerId = manager.getId();
        Long departmentId = manager.getDepartment().getId();

        // Закрыть текущий CALLED (если есть)
        ticketRepository.finishCurrentForManager(managerId);

        // Взять следующий WAITING с блокировкой
        Long nextId = ticketRepository.selectNextWaitingIdForUpdate(departmentId)
                .orElseThrow(() -> new NotFoundException("No waiting tickets found for manager: " + manager.getUser().getEmail()));

        // Назначить его текущему менеджеру
        ticketRepository.claimById(nextId, managerId);

        Ticket nextTicket = ticketRepository.findById(nextId)
                .orElseThrow(() -> new IllegalStateException("Ticket claimed but not found: " + nextId));

        emitters.sendToAll(TicketMapper.TICKET_MAPPER.toDto(nextTicket));
        // Вернуть номер талона
        return ticketRepository.findTicketNumberById(nextId)
                .orElseThrow(() -> new IllegalStateException("Ticket claimed but not found: " + nextId));
    }
}
