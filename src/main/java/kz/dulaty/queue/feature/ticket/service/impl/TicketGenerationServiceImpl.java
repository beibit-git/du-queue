package kz.dulaty.queue.feature.ticket.service.impl;

import kz.dulaty.queue.feature.ticket.data.dto.TicketDto;
import kz.dulaty.queue.feature.ticket.data.dto.TicketRequestDto;
import kz.dulaty.queue.feature.ticket.data.dto.WsEventDto;
import kz.dulaty.queue.feature.department.data.entity.Department;
import kz.dulaty.queue.feature.ticket.data.entity.Ticket;
import kz.dulaty.queue.feature.ticket.data.enums.TicketStatus;
import kz.dulaty.queue.feature.ticket.data.mapper.TicketMapper;
import kz.dulaty.queue.feature.department.data.repository.DepartmentRepository;
import kz.dulaty.queue.feature.ticket.data.repository.TicketRepository;
import kz.dulaty.queue.feature.ticket.service.TicketGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketGenerationServiceImpl implements TicketGenerationService {
    private final TicketRepository ticketRepository;
    private final DepartmentRepository departmentRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String QUEUE_EVENTS_TOPIC = "/topic/queue-events";

    @Override
    public TicketDto generateTicket(TicketRequestDto request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        Ticket existing = ticketRepository.findByPhoneNumberAndDepartmentAndTicketStatus(
                request.getPhoneNumber(), department, TicketStatus.WAITING);

        if (existing != null) {
            log.info("Existing WAITING ticket returned for phone: {}", request.getPhoneNumber());
            return TicketMapper.TICKET_MAPPER.toDto(existing);
        }

        Ticket newTicket = new Ticket();
        newTicket.setPhoneNumber(request.getPhoneNumber());
        newTicket.setDepartment(department);
        newTicket.setTicketNumber(ticketRepository.getNextTicketNumber(request.getDepartmentId().intValue()));
        newTicket.setTrackingToken(UUID.randomUUID().toString());
        newTicket.setTicketStatus(TicketStatus.WAITING);
        ticketRepository.save(newTicket);

        TicketDto dto = TicketMapper.TICKET_MAPPER.toDto(newTicket);
        messagingTemplate.convertAndSend(QUEUE_EVENTS_TOPIC, WsEventDto.ticketGenerated(dto));
        log.debug("WS TICKET_GENERATED sent: {}", newTicket.getTicketNumber());

        return dto;
    }
}
