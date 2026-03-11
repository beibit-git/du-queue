package kz.dulaty.queue.feature.advertisement.data.entity;

import jakarta.persistence.*;
import kz.dulaty.queue.feature.auth.data.entity.BaseEntity;
import lombok.*;

@Entity
@Table(name = "advertisements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "youtube_url", nullable = false, length = 500)
    private String youtubeUrl;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
