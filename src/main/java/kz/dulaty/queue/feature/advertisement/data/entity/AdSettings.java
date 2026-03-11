package kz.dulaty.queue.feature.advertisement.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ad_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdSettings {

    @Id
    private Long id;

    @Column(name = "ads_enabled", nullable = false)
    private Boolean adsEnabled = false;

    @Column(name = "switch_interval_seconds", nullable = false)
    private Integer switchIntervalSeconds = 30;
}
