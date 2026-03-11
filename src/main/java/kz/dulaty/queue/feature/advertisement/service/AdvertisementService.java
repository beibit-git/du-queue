package kz.dulaty.queue.feature.advertisement.service;

import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.advertisement.data.dto.*;

import java.util.List;

public interface AdvertisementService {

    AdDisplayResponse getDisplayData();

    List<AdvertisementDto> getAllForAdmin();

    AdvertisementDto create(AdvertisementRequestDto request);

    AdvertisementDto update(Long id, AdvertisementRequestDto request) throws NotFoundException;

    void delete(Long id) throws NotFoundException;

    AdSettingsDto getSettings();

    AdSettingsDto updateSettings(AdSettingsDto settings);
}
