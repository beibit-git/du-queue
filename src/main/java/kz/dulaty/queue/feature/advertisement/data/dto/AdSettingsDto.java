package kz.dulaty.queue.feature.advertisement.data.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AdSettingsDto(
        @NotNull Boolean adsEnabled,
        @NotNull @Min(5) Integer switchIntervalSeconds
) {}
