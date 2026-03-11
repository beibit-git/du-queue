package kz.dulaty.queue.feature.advertisement.data.repository;

import kz.dulaty.queue.feature.advertisement.data.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    List<Advertisement> findAllByOrderByOrderIndexAsc();

    List<Advertisement> findByIsActiveTrueOrderByOrderIndexAsc();
}
