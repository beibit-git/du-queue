package kz.dulaty.queue.feature.data.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TicketStatus {
    WAITING,
    CALLED,
    DONE
}
