package kz.dulaty.queue.feature.ticket.service.impl;

import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.manager.data.entity.Manager;
import kz.dulaty.queue.feature.manager.data.repository.ManagerRepository;
import kz.dulaty.queue.feature.ticket.data.dto.TicketDto;
import kz.dulaty.queue.feature.ticket.data.dto.WsEventDto;
import kz.dulaty.queue.feature.ticket.data.entity.Ticket;
import kz.dulaty.queue.feature.ticket.data.enums.TicketStatus;
import kz.dulaty.queue.feature.ticket.data.mapper.TicketMapper;
import kz.dulaty.queue.feature.ticket.data.repository.TicketRepository;
import kz.dulaty.queue.feature.ticket.service.TicketCallingService;
import kz.dulaty.queue.feature.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketCallingServiceImpl implements TicketCallingService {
    private final TicketRepository ticketRepository;
    private final ManagerRepository managerRepository;
    private final SseService sseService;

    @Override
    @Transactional
    public TicketDto callNextTicket(String email) throws NotFoundException {
        Manager manager = managerRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("Manager not found with email: " + email));

        Long managerId = manager.getId();
        Long departmentId = manager.getDepartment().getId();

        // Получаем текущий CALLED талон менеджера до завершения (для TICKET_DONE события)
        Ticket currentCalled = ticketRepository.findCurrentCalledForManager(managerId).orElse(null);

        // Закрыть текущий CALLED
        ticketRepository.finishCurrentForManager(managerId);

        // Подготовить TICKET_DONE событие (до коммита)
        final WsEventDto doneEvent;
        if (currentCalled != null) {
            currentCalled.setTicketStatus(TicketStatus.DONE);
            doneEvent = WsEventDto.ticketDone(TicketMapper.TICKET_MAPPER.toDto(currentCalled));
        } else {
            doneEvent = null;
        }

        // Взять следующий WAITING с блокировкой
        Long nextId = ticketRepository.selectNextWaitingIdForUpdate(departmentId)
                .orElseThrow(() -> new NotFoundException("No waiting tickets found for department: " + departmentId));

        // Назначить менеджеру
        ticketRepository.claimById(nextId, managerId);

        Ticket nextTicket = ticketRepository.findById(nextId)
                .orElseThrow(() -> new IllegalStateException("Ticket claimed but not found: " + nextId));

        final WsEventDto calledEvent = WsEventDto.ticketCalled(TicketMapper.TICKET_MAPPER.toDto(nextTicket));

        // Отправлять SSE ТОЛЬКО после коммита транзакции — иначе клиент получит
        // уведомление раньше чем данные появятся в БД
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                if (doneEvent != null) {
                    sseService.broadcast(doneEvent);
                    log.debug("SSE TICKET_DONE sent after commit");
                }
                sseService.broadcast(calledEvent);
                log.debug("SSE TICKET_CALLED sent after commit: {}", nextTicket.getTicketNumber());
            }
        });

        return TicketMapper.TICKET_MAPPER.toDto(nextTicket);
    }
}
