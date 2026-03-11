package kz.dulaty.queue.feature.advertisement.data.dto;

public record AdvertisementDto(
        Long id,
        String youtubeUrl,
        Integer orderIndex,
        Boolean isActive
) {}
