package com.travel.domain.product.controller;

import com.travel.domain.product.dto.request.AccommodationRequest;
import com.travel.domain.product.dto.response.AccommodationDetailListResponse;
import com.travel.domain.product.dto.response.ProductDetailResponse;
import com.travel.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accommodations/{accommodation_id}")
public class ProductController {

    private final ProductService productService;

    //숙소 id를 통해 숙소 조회(이미지, 정보, 옵션), 객실 전체 조회 -> 인원수, 날짜 통해 가져옴. 날짜 비교는 accommodationinfopernight
    @GetMapping
    public ResponseEntity<AccommodationDetailListResponse> getAccommodationDetail(
        @PathVariable Long accommodation_id,
        @RequestParam(required = false) LocalDate checkIn,
        @RequestParam(required = false) LocalDate checkOut,
        @RequestParam(required = false) Integer personNumber
    ) {
        AccommodationRequest request = new AccommodationRequest(checkIn, checkOut, personNumber);
        var response = productService.getAccommodationDetail(accommodation_id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomType}/{product_id}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(
        @PathVariable Long accommodation_id,
        @RequestParam String roomType,
        @PathVariable Long product_id,
        @RequestParam(required = false) LocalDate checkIn,
        @RequestParam(required = false) LocalDate checkOut,
        @RequestParam(required = false) Integer personNumber
    ) {
        AccommodationRequest request = new AccommodationRequest(checkIn, checkOut, personNumber);
        var response = productService.getProductDetail(accommodation_id, roomType, product_id, request);
        return ResponseEntity.ok(response);
    }

}


/*

public ResponseEntity<List<AccommodationResponse>> getAllAccommodations()
AccommodationResponse -> AccommodationListResponse
AccommodationListResponse
ResponseEntity
List<AccommodationResponse>
 */