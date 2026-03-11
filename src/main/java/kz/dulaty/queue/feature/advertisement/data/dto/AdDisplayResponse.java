package kz.dulaty.queue.feature.advertisement.data.dto;

import java.util.List;

public record AdDisplayResponse(
        boolean adsEnabled,
        int switchIntervalSeconds,
        List<AdvertisementDto> videos
) {}
