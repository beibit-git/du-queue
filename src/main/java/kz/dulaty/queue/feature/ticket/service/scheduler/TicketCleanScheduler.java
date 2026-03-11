package kz.dulaty.queue.feature.ticket.service.scheduler;

import kz.dulaty.queue.core.properties.SchedulingProperties;
import kz.dulaty.queue.feature.ticket.data.dto.WsEventDto;
import kz.dulaty.queue.feature.ticket.data.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketCleanScheduler {
    private final SchedulingProperties schedulingProperties;
    private final TicketRepository ticketRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String QUEUE_EVENTS_TOPIC = "/topic/queue-events";

    @Scheduled(cron = "#{@schedulingProperties.ticketCleaner}")
    @SchedulerLock(name = "cleanTickets", lockAtLeastFor = "PT30s", lockAtMostFor = "PT60s")
    public void cleanTickets() {
        log.info("Starting daily ticket cleanup...");
        ticketRepository.cleanCounters();
        ticketRepository.deleteAll();

        // Оповестить все подключённые клиенты о сбросе очереди
        messagingTemplate.convertAndSend(QUEUE_EVENTS_TOPIC, WsEventDto.queueReset());
        log.info("Ticket cleanup completed. QUEUE_RESET event sent.");
    }
}
