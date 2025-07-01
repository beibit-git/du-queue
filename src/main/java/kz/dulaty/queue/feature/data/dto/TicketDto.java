package kz.dulaty.queue.feature.data.dto;

import kz.dulaty.queue.feature.data.entity.Department;
import kz.dulaty.queue.feature.data.enums.TicketStatus;
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
