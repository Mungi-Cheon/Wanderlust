package com.travel.domain.accommodation.dto.request;

import com.travel.domain.accommodation.category.Category;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@AllArgsConstructor
@ParameterObject
public class AccommodationRequest {

    private Integer categoryId;

    private String keyword;

    private Long lastAccommodationId;

    private String checkInDate;

    private String checkOutDate;
}
