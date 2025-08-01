package kz.dulaty.queue.feature.ticket.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TicketStatus {
    WAITING,
    CALLED,
    DONE
}
