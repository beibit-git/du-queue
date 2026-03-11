package kz.dulaty.queue.feature.advertisement.service.impl;

import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.advertisement.data.dto.*;
import kz.dulaty.queue.feature.advertisement.data.entity.AdSettings;
import kz.dulaty.queue.feature.advertisement.data.entity.Advertisement;
import kz.dulaty.queue.feature.advertisement.data.repository.AdSettingsRepository;
import kz.dulaty.queue.feature.advertisement.data.repository.AdvertisementRepository;
import kz.dulaty.queue.feature.advertisement.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final AdSettingsRepository adSettingsRepository;

    private AdSettings getOrCreateSettings() {
        return adSettingsRepository.findById(1L).orElseGet(() -> {
            AdSettings s = new AdSettings(1L, false, 30);
            return adSettingsRepository.save(s);
        });
    }

    @Override
    public AdDisplayResponse getDisplayData() {
        AdSettings settings = getOrCreateSettings();
        List<AdvertisementDto> videos = advertisementRepository
                .findByIsActiveTrueOrderByOrderIndexAsc()
                .stream()
                .map(this::toDto)
                .toList();
        return new AdDisplayResponse(settings.getAdsEnabled(), settings.getSwitchIntervalSeconds(), videos);
    }

    @Override
    public List<AdvertisementDto> getAllForAdmin() {
        return advertisementRepository.findAllByOrderByOrderIndexAsc()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public AdvertisementDto create(AdvertisementRequestDto request) {
        Advertisement ad = new Advertisement();
        ad.setYoutubeUrl(request.youtubeUrl());
        ad.setOrderIndex(request.orderIndex() != null ? request.orderIndex() : 0);
        ad.setIsActive(request.isActive() != null ? request.isActive() : Boolean.TRUE);
        return toDto(advertisementRepository.save(ad));
    }

    @Override
    public AdvertisementDto update(Long id, AdvertisementRequestDto request) throws NotFoundException {
        Advertisement ad = advertisementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Advertisement not found"));
        ad.setYoutubeUrl(request.youtubeUrl());
        if (request.orderIndex() != null) ad.setOrderIndex(request.orderIndex());
        if (request.isActive() != null) ad.setIsActive(request.isActive());
        return toDto(advertisementRepository.save(ad));
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        if (!advertisementRepository.existsById(id)) {
            throw new NotFoundException("Advertisement not found");
        }
        advertisementRepository.deleteById(id);
    }

    @Override
    public AdSettingsDto getSettings() {
        AdSettings s = getOrCreateSettings();
        return new AdSettingsDto(s.getAdsEnabled(), s.getSwitchIntervalSeconds());
    }

    @Override
    public AdSettingsDto updateSettings(AdSettingsDto dto) {
        AdSettings s = getOrCreateSettings();
        s.setAdsEnabled(dto.adsEnabled());
        s.setSwitchIntervalSeconds(dto.switchIntervalSeconds());
        adSettingsRepository.save(s);
        return new AdSettingsDto(s.getAdsEnabled(), s.getSwitchIntervalSeconds());
    }

    private AdvertisementDto toDto(Advertisement ad) {
        return new AdvertisementDto(ad.getId(), ad.getYoutubeUrl(), ad.getOrderIndex(), ad.getIsActive());
    }
}
