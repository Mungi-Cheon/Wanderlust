package com.travel.domain.accommodation.service;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.type.ErrorType;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccommodationService {

  private final AccommodationRepository accommodationRepository;

  public AccommodationService(AccommodationRepository accommodationRepository) {
    this.accommodationRepository = accommodationRepository;
  }

  @Transactional(readOnly = true)
  public List<AccommodationResponse> getAllAccommodationsByCategory(String category) {
    List<Accommodation> accommodations = accommodationRepository.findByCategory(category);
    if (accommodations.isEmpty()) {
      throw new AccommodationException(ErrorType.NOT_FOUND);
    }
    return accommodations.stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<AccommodationResponse> getAvailableAccommodations(String category, LocalDate checkIn,
      LocalDate checkOut, int guestCount) {
    List<Accommodation> accommodations = accommodationRepository.findAvailableAccommodations(category,
        checkIn, checkOut, guestCount);
    if (accommodations.isEmpty()) {
      throw new AccommodationException(ErrorType.NOT_FOUND);
    }
    return accommodations.stream()
        .map(this::convertToResponse)
        .collect(Collectors.toList());
  }

  private AccommodationResponse convertToResponse(Accommodation accommodation) {
    int price = accommodation.getProducts().stream()
        .filter(product -> "standard".equalsIgnoreCase(product.getType()))
        .flatMap(product -> product.getProductInfoPerNightsList().stream())
        .mapToInt(ProductInfoPerNight::getPrice)
        .findFirst()
        .orElse(0);

    String thumbnail = accommodation.getImages().getThumbnail();

    return new AccommodationResponse(accommodation.getId(), accommodation.getName(), price, thumbnail,
        accommodation.getCategory(), accommodation.getGrade());
  }
}
