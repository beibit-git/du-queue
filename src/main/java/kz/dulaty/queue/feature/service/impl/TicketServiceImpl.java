package kz.dulaty.queue.feature.service.impl;

import kz.dulaty.queue.feature.data.dto.TicketDto;
import kz.dulaty.queue.feature.data.dto.TicketRequestDto;
import kz.dulaty.queue.feature.data.entity.Department;
import kz.dulaty.queue.feature.data.entity.Ticket;
import kz.dulaty.queue.feature.data.enums.TicketStatus;
import kz.dulaty.queue.feature.data.mapper.TicketMapper;
import kz.dulaty.queue.feature.data.repository.DepartmentRepository;
import kz.dulaty.queue.feature.data.repository.TicketRepository;
import kz.dulaty.queue.feature.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public TicketDto generateTicket(TicketRequestDto request) {

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        Ticket ticket = ticketRepository.findByPhoneNumberAndDepartmentAndTicketStatus(
                request.getPhoneNumber(),
                department,
                TicketStatus.WAITING);

        if(ticket != null) {
            log.info("Ticket already exists for phone number: {}", request.getPhoneNumber());
            return TicketMapper.TICKET_MAPPER.toDto(ticket);
        } else {
            Ticket newTicket = new Ticket();
            newTicket.setPhoneNumber(request.getPhoneNumber());
            newTicket.setDepartment(department);
            newTicket.setTicketNumber(ticketRepository.getNextTicketNumber(request.getDepartmentId().intValue()));
            newTicket.setTrackingToken(UUID.randomUUID().toString());
            newTicket.setTicketStatus(TicketStatus.WAITING);
            ticketRepository.save(newTicket);
            return TicketMapper.TICKET_MAPPER.toDto(newTicket);
        }
    }
}
