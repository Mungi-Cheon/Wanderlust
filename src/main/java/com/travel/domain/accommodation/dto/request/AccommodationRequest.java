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

    @NotNull(message = "체크인 날짜를 입력해주세요.")
    private LocalDate checkInDate;

    @NotNull(message = "체크아웃 날짜를 입력해주세요.")
    private LocalDate checkOutDate;

    @Min(value = 1, message = "숙박 인원 수는 최소 1명 이상이어야 합니다.")
    private Integer personNumber;

    @AssertTrue(message = "체크인 날짜는 오늘 또는 이후 날짜여야 합니다.")
    public boolean isCheckInValid() {
        return checkInDate != null && !checkInDate.isBefore(LocalDate.now());
    }

    @AssertTrue(message = "체크아웃 날짜는 체크인 날짜 이후여야 합니다.")
    public boolean isCheckOutValid() {
        return checkInDate != null && checkOutDate != null && checkOutDate.isAfter(checkInDate);
    }

    @AssertTrue(message = "잘못된 카테고리입니다.")
    public boolean isCategoryIdValid() {
        return categoryId == null || Arrays.stream(Category.values())
            .anyMatch(category -> category.getId().equals(categoryId));
    }

}
