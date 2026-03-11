package kz.dulaty.queue.feature.ticket.data.dto;

import kz.dulaty.queue.feature.ticket.data.enums.WsEventType;

/**
 * Обёртка для всех WebSocket-событий очереди.
 * Отправляется в topic /topic/queue-events
 */
public record WsEventDto(
        WsEventType eventType,
        TicketDto ticket   // null только для QUEUE_RESET
) {
    public static WsEventDto ticketGenerated(TicketDto ticket) {
        return new WsEventDto(WsEventType.TICKET_GENERATED, ticket);
    }

    public static WsEventDto ticketCalled(TicketDto ticket) {
        return new WsEventDto(WsEventType.TICKET_CALLED, ticket);
    }

    public static WsEventDto ticketDone(TicketDto ticket) {
        return new WsEventDto(WsEventType.TICKET_DONE, ticket);
    }

    public static WsEventDto queueReset() {
        return new WsEventDto(WsEventType.QUEUE_RESET, null);
    }
}
