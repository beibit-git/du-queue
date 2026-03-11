package kz.dulaty.queue.feature.advertisement.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.dulaty.queue.core.exception.NotFoundException;
import kz.dulaty.queue.feature.advertisement.data.dto.*;
import kz.dulaty.queue.feature.advertisement.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Рекламные ролики")
@RestController
@RequestMapping("/api/v1/ads")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    /** Публичный эндпоинт — возвращает настройки и активные ролики для отображения */
    @GetMapping
    @PreAuthorize("permitAll()")
    public AdDisplayResponse getDisplayData() {
        return advertisementService.getDisplayData();
    }

    /** Все ролики для панели администратора (включая неактивные) */
    @GetMapping("/admin/videos")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public List<AdvertisementDto> getAllForAdmin() {
        return advertisementService.getAllForAdmin();
    }

    @PostMapping("/video")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public AdvertisementDto create(@RequestBody @Valid AdvertisementRequestDto request) {
        return advertisementService.create(request);
    }

    @PutMapping("/video/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public AdvertisementDto update(@PathVariable Long id,
                                   @RequestBody @Valid AdvertisementRequestDto request) throws NotFoundException {
        return advertisementService.update(id, request);
    }

    @DeleteMapping("/video/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws NotFoundException {
        advertisementService.delete(id);
    }

    @GetMapping("/settings")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public AdSettingsDto getSettings() {
        return advertisementService.getSettings();
    }

    @PutMapping("/settings")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    public AdSettingsDto updateSettings(@RequestBody @Valid AdSettingsDto dto) {
        return advertisementService.updateSettings(dto);
    }
}
