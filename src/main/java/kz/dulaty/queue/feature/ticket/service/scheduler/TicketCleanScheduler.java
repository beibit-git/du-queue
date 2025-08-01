package kz.dulaty.queue.feature.ticket.service.scheduler;

import kz.dulaty.queue.core.properties.SchedulingProperties;
import kz.dulaty.queue.feature.ticket.data.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class TicketCleanScheduler {
    private final SchedulingProperties schedulingProperties;
    private final TicketRepository ticketRepository;
    @Scheduled(cron = "#{@schedulingProperties.ticketCleaner}")
    @SchedulerLock(name = "cleanTickets", lockAtLeastFor = "PT30s", lockAtMostFor = "PT60s")
    public void cleanTickets() {
        log.info("Cleaning ticket counters");
        ticketRepository.cleanCounters();
        ticketRepository.deleteAll();
        log.info("Ticket counters cleaned successfully");
    }
}
