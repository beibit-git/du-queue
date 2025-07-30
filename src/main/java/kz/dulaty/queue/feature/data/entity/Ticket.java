package kz.dulaty.queue.feature.data.entity;

import jakarta.persistence.*;
import kz.dulaty.queue.feature.auth.data.entity.BaseEntity;
import kz.dulaty.queue.feature.data.enums.TicketStatus;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Entity
@Getter
@Setter
@ToString
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String phoneNumber;
    private String ticketNumber;
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;
    @ManyToOne
    private Department department;
    @ManyToOne
    private Manager manager;
    private String trackingToken;
    private Boolean smsSent = false;
}
