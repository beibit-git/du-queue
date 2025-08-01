package kz.dulaty.queue.feature.ticket.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TicketRequestDto {
    private String phoneNumber;
    private Long departmentId;
}
