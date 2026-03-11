package kz.dulaty.queue.feature.advertisement.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdvertisementRequestDto(
        @NotBlank String youtubeUrl,
        @NotNull Integer orderIndex,
        Boolean isActive
) {}
