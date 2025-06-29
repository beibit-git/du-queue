package kz.dulaty.queue.feature.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ticket_counters")
public class TicketCounters {
    @Id
    @OneToOne
    private Department department;
    private Integer currentTicketNumber;
    private LocalDateTime updatedTime;
}
