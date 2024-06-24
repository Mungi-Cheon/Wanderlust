package com.travel.domain.accommodation.service;

import static com.travel.global.util.DateValidationUtil.isCheckInValid;
import static com.travel.global.util.DateValidationUtil.isCheckOutValid;

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
  public List<AccommodationResponse> getAvailableAccommodations(String category, LocalDate checkIn, LocalDate checkOut, int guestCount) {
    if(!isCheckInValid(checkIn)) {
      throw new AccommodationException(ErrorType.INVALID_CHECK_IN);
    }

    if(!isCheckOutValid(checkIn, checkOut)) {
      throw new AccommodationException(ErrorType.INVALID_CHECK_OUT);
    }

    if(guestCount<1) {
      throw new AccommodationException(ErrorType.INVALID_NUMBER_OF_PEOPLE);
    }

    List<Accommodation> accommodations = accommodationRepository.findAvailableAccommodations(category, checkIn, checkOut, guestCount);
    if (accommodations.isEmpty()) {
      throw new AccommodationException(ErrorType.NOT_FOUND);
    }

    List<Accommodation> validAccommodationList = new ArrayList<>();

    for(Accommodation accommodation : accommodations) {
      List<Product> productEntityList = productRepository.findAllByAccommodationId(accommodation.getId());

      for (Product product : productEntityList) {
        boolean allDatesExist = true;

        for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
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

    return validAccommodationList.stream()
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
