package kz.dulaty.queue.feature.ticket.data.enums;

public enum WsEventType {
    /** Новый талон создан, статус WAITING */
    TICKET_GENERATED,
    /** Менеджер вызвал талон, статус CALLED */
    TICKET_CALLED,
    /** Талон обслужен менеджером, статус DONE */
    TICKET_DONE,
    /** Ежедневный сброс очереди */
    QUEUE_RESET
}
