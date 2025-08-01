package kz.dulaty.queue.feature.ticket.data.dto;

import kz.dulaty.queue.feature.department.data.entity.Department;
import kz.dulaty.queue.feature.ticket.data.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TicketDto {
    private String phoneNumber;
    private String ticketNumber;
    private TicketStatus ticketStatus;
    private Department department;
}
