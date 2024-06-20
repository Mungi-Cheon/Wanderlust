package com.travel.domain.product.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotNull(message = "체크인 시간을 지정해주세요")
    private String checkInTime;
    @NotNull(message = "체크아웃 시간을 지정해주세요")
    private String checkOutTime;
    @NotNull(message = "인원을 선택해 주세요")
    private int personnel;
    private String roomType;
}
