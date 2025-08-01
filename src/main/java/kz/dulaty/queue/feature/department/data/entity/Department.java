package kz.dulaty.queue.feature.department.data.entity;

import jakarta.persistence.*;
import kz.dulaty.queue.feature.auth.data.entity.BaseEntity;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Entity
@Getter
@Setter
@ToString
@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "departments")
public class Department extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "full_name")
    private String fullName;

    private String prefix;
}
