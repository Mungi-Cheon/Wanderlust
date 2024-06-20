package com.travel.domain.accommodation.service;

import com.travel.domain.accommodation.dto.request.AccommodationRequest;
import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.repository.ProductInfoPerNightRepository;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.ProductException;
import com.travel.global.exception.type.ErrorType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccommodationService {

  private final ProductRepository productRepository;
  private final AccommodationRepository accommodationRepository;
  private final ProductInfoPerNightRepository productInfoPerNightRepository;

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
  public List<AccommodationResponse> getAvailableAccommodations(String category, AccommodationRequest request) {
    List<Accommodation> accommodations = accommodationRepository.findAvailableAccommodations(category,
        request.getCheckIn(), request.getCheckOut(), request.getGuestCount());
    if (accommodations.isEmpty()) {
      throw new AccommodationException(ErrorType.NOT_FOUND);
    }

    List<Accommodation> validAccommodationList = new ArrayList<>();

    for(Accommodation accommodation : accommodations) {
      List<Product> productEntityList = productRepository.findAllByAccommodationId(accommodation.getId());

      for (Product product : productEntityList) {
        boolean allDatesExist = true;

        for (LocalDate date = request.getCheckIn(); date.isBefore(request.getCheckOut()); date = date.plusDays(1)) {
          if (!productInfoPerNightRepository.existsByProductIdAndDate(product.getId(), date)) {
            allDatesExist = false;
            break;
          }
        }

        if (allDatesExist) {
          validAccommodationList.add(accommodation);
          break;
        }
      }
    }

    if(validAccommodationList.isEmpty()) {
      throw new ProductException(ErrorType.NOT_FOUND);
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
