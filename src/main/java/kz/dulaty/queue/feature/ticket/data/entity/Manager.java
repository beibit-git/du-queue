package kz.dulaty.queue.feature.ticket.data.entity;

import jakarta.persistence.*;
import kz.dulaty.queue.feature.auth.data.entity.BaseEntity;
import kz.dulaty.queue.feature.auth.data.entity.User;
import kz.dulaty.queue.feature.department.data.entity.Department;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Entity
@Getter
@Setter
@ToString
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "managers")
public class Manager extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String windowNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    private Department department;
    @OneToOne
    private User user;
}
