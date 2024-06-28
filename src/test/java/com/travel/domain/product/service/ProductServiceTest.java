//package com.travel.domain.product.service;
//
//import com.travel.domain.accommodation.dto.response.AccommodationDetailListResponse;
//import com.travel.domain.accommodation.dto.response.AccommodationImageResponse;
//import com.travel.domain.accommodation.dto.response.AccommodationOptionResponse;
//import com.travel.domain.accommodation.dto.response.AccommodationResponse;
//import com.travel.domain.accommodation.entity.Accommodation;
//import com.travel.domain.accommodation.entity.AccommodationImage;
//import com.travel.domain.accommodation.entity.AccommodationOption;
//import com.travel.domain.accommodation.repository.AccommodationRepository;
//import com.travel.domain.accommodation.service.AccommodationService;
//import com.travel.domain.product.dto.response.ProductDetailResponse;
//import com.travel.domain.product.dto.response.ProductImageResponse;
//import com.travel.domain.product.dto.response.ProductOptionResponse;
//import com.travel.domain.product.dto.response.ProductResponse;
//import com.travel.domain.product.entity.Product;
//import com.travel.domain.product.entity.ProductImage;
//import com.travel.domain.product.entity.ProductInfoPerNight;
//import com.travel.domain.product.entity.ProductOption;
//import com.travel.domain.product.repository.ProductInfoPerNightRepository;
//import com.travel.domain.product.repository.ProductRepository;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.core.parameters.P;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//@AutoConfigureMockMvc
//class ProductServiceTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    ProductRepository productRepository;
//
//    @MockBean
//    ProductInfoPerNightRepository productInfoPerNightRepository;
//
//    @MockBean
//    AccommodationRepository accommodationRepository;
//
//    Long accommodationId = 1L;
//    Long productId = 1L;
//    LocalDate checkInDate = LocalDate.now();
//    LocalDate checkOutDate = LocalDate.now().plusDays(2);
//    int personNumber = 2;
//    Long productImageId = 1L;
//    Long productInfoPerNightId = 1L;
//    Long accommodationOptionId = 1L;
//    Long accommodationImageId = 1L;
//    LocalDate infoDate = checkInDate.plusDays(1);
//    String checkInTime = "15:00";
//    String checkOutTime = "11:00";
//    int standardNumber = 2;
//    int maximumNumber = 3;
//
//    @Autowired
//    private AccommodationService accommodationService;
//    @Autowired
//    private ProductService productService;
//
//    private AccommodationDetailListResponse createAccommodationDetailListResponse(
//        Accommodation accommodation, AccommodationImageResponse accommodationImageResponse,
//        AccommodationOptionResponse accommodationOptionResponse,
//        List<ProductResponse> productResponse) {
//        return AccommodationDetailListResponse.builder()
//            .id(accommodationId)
//            .name(accommodation.getName())
//            .description(accommodation.getDescription())
//            .checkInDate(checkInDate)
//            .checkOutDate(checkOutDate)
//            .accommodationImage(accommodationImageResponse)
//            .accommodationOption(accommodationOptionResponse)
//            .productResponseList(productResponse)
//            .build();
//    }
//
//    private AccommodationImageResponse createAccommodationImageResponse() {
//        return AccommodationImageResponse.builder()
//            .imageUrl1("image1")
//            .imageUrl2("image2")
//            .build();
//    }
//
//    private AccommodationOptionResponse createAccommodationOptionResponse() {
//        return AccommodationOptionResponse.builder()
//            .hasSmokingRoom(true)
//            .hasCooking(true)
//            .hasParking(true)
//            .hasSwimmingPool(false)
//            .hasBreakfast(true)
//            .hasFitness(true)
//            .hasBeauty(true)
//            .build();
//    }
//
//    private ProductResponse createProductResponse(
//        ProductImageResponse productImageResponse, Product product) {
//        return ProductResponse.builder()
//            .id(productId)
//            .name(product.getName())
//            .checkInTime(product.getCheckInTime())
//            .checkOutTime(product.getCheckOutTime())
//            .pricePerNight(100000)
//            .standardNumber(product.getStandardNumber())
//            .maximumNumber(product.getMaximumNumber())
//            .images(productImageResponse)
//            .count(2)
//            .build();
//
//    }
//
//    private ProductImageResponse createProductImageResponse() {
//        return ProductImageResponse.builder()
//            .imageUrl1("img1")
//            .imageUrl2("img2")
//            .build();
//    }
//
//
//    private Accommodation createAccommodation(
//        AccommodationOption accommodationOption, AccommodationImage accommodationImage,
//        List<Product> productList) {
//        return Accommodation.builder()
//            .id(accommodationId)
//            .name("accommodationName")
//            .contact("064-729-8100")
//            .description("accommodation description")
//            .address("제주특별자치도 제주시 탑동로 66")
//            .category("호텔")
//            .grade(BigDecimal.valueOf(4.1))
//            .options(accommodationOption)
//            .images(accommodationImage)
//            .products(productList)
//            .build();
//    }
//
//    private AccommodationOption createAccommodationOption(Accommodation accommodation) {
//        return AccommodationOption.builder()
//            .id(accommodationOptionId)
//            .accommodation(accommodation)
//            .hasSmokingRoom(true)
//            .hasCooking(true)
//            .hasParking(true)
//            .hasSwimmingPool(false)
//            .hasBreakfast(true)
//            .hasFitness(true)
//            .hasBeauty(true)
//            .build();
//    }
//
//    private AccommodationImage createAccommodationImage(Accommodation accommodation) {
//        return AccommodationImage.builder()
//            .id(accommodationImageId)
//            .accommodation(accommodation)
//            .thumbnail("thumnail")
//            .imageUrl1("img1")
//            .imageUrl2("img2")
//            .build();
//    }
//
//    private Product createProduct(
//        Accommodation accommodation, ProductImage productImage,
//        List<ProductInfoPerNight> productInfoPerNightList, ProductOption productOption) {
//        return Product.builder()
//            .id(productId)
//            .accommodation(accommodation)
//            .name("ProductName")
//            .description("ProductDescription")
//            .checkInTime(checkInTime)
//            .checkOutTime(checkOutTime)
//            .standardNumber(standardNumber)
//            .maximumNumber(maximumNumber)
//            .type("Standard")
//            .productOption(productOption)
//            .productInfoPerNightsList(productInfoPerNightList)
//            .productImage(productImage)
//            .build();
//
//    }
//
//    private ProductOption createProductOption() {
//        return ProductOption.builder()
//            .hasBath(true)
//            .hasAirCondition(true)
//            .hasTv(true)
//            .hasPc(true)
//            .hasWifi(true)
//            .hasCable(true)
//            .hasRefrigerator(true)
//            .hasSofa(true)
//            .canCook(true)
//            .hasTable(true)
//            .hasHairdryer(true)
//            .build();
//    }
//
//    private ProductImage createProductImage(Product product, Accommodation accommodation) {
//        return ProductImage.builder()
//            .id(productImageId)
//            .product(product)
//            .imageUrl1("img1")
//            .imageUrl2("img2")
//            .accommodation(accommodation)
//            .build();
//    }
//
//    private ProductInfoPerNight createProductInfoPerNight(Product product, Accommodation accommodation) {
//        return ProductInfoPerNight.builder()
//            .id(productInfoPerNightId)
//            .product(product)
//            .accommodation(accommodation)
//            .date(infoDate)
//            .price(400000)
//            .count(2)
//            .build();
//    }
//
//    private ProductDetailResponse createProductDetailResponse(
//        Product product, ProductImageResponse productImageResponse,
//        ProductOptionResponse productOptionResponse) {
//        return ProductDetailResponse.builder()
//            .id(productId)
//            .name(product.getName())
//            .accommodationName("accommodationName")
//            .description(product.getDescription())
//            .pricePerNight(100000)
//            .totalPrice(200000)
//            .numberOfStay(2)
//            .standardNumber(2)
//            .maximumNumber(3)
//            .type(product.getType())
//            .productImageResponse(productImageResponse)
//            .productOption(productOptionResponse)
//            .build();
//    }
//
//    private ProductOptionResponse createProductOptionResponse() {
//        return ProductOptionResponse.builder()
//            .hasBath(true)
//            .hasAirCondition(true)
//            .hasTv(true)
//            .hasPc(true)
//            .hasWifi(true)
//            .hasCable(true)
//            .hasRefrigerator(true)
//            .hasSofa(true)
//            .canCook(true)
//            .hasTable(true)
//            .hasHairdryer(true)
//            .build();
//    }
//
//
//    @BeforeAll
//    static void setUp() {
//    }
//
//    @Test
//    @DisplayName("숙박 디테일, 객실 리스트 조회")
//    void getAccommodationDetail() {
//        //given
//        Accommodation accommodation = new Accommodation();
//        AccommodationOption accommodationOption = createAccommodationOption(accommodation);
//        AccommodationImage accommodationImage = createAccommodationImage(accommodation);
//        Product product = new Product();
//        List<Product> productList = new ArrayList<>();
//        List<ProductInfoPerNight> productInfoPerNightList = new ArrayList<>();
//        accommodation = createAccommodation(accommodationOption, accommodationImage, productList);
//        ProductOption productOption = createProductOption();
//        ProductImage productImage = createProductImage(product, accommodation);
//        ProductInfoPerNight productInfoPerNight = createProductInfoPerNight(product, accommodation);
//        productInfoPerNightList.add(productInfoPerNight);
//        product = createProduct(accommodation, productImage, productInfoPerNightList, productOption);
//        productList.add(product);
//        AccommodationOptionResponse accommodationOptionResponse = createAccommodationOptionResponse();
//        AccommodationImageResponse accommodationImageResponse = createAccommodationImageResponse();
//        ProductImageResponse productImageResponse = createProductImageResponse();
//        ProductResponse productResponse = createProductResponse(productImageResponse, product);
//        List<ProductResponse> productResponsesList = new ArrayList<>();
//        productResponsesList.add(productResponse);
//        AccommodationDetailListResponse accommodationDetailListResponse
//            = createAccommodationDetailListResponse(accommodation, accommodationImageResponse,
//            accommodationOptionResponse, productResponsesList);
//
//        //when
//        when(accommodationRepository
//            .findById(accommodationId)).thenReturn(Optional.of(accommodation));
//        when(productRepository
//            .findAllByAccommodationId(accommodationId)).thenReturn(productList);
//        when(productInfoPerNightRepository
//            .existsByProductIdAndDate(eq(productId), any(LocalDate.class))).thenReturn(true);
//        when(productInfoPerNightRepository
//            .findMinCountByProductIdAndDateRange(eq(productId), eq(checkInDate), eq(checkOutDate))).thenReturn(2);
//
//        AccommodationDetailListResponse response
//            = productService.getAccommodationDetail(accommodationId, checkInDate, checkOutDate, personNumber);
//
//        assertNotNull(response);
//        assertEquals(accommodationId, accommodation.getId());
//        assertEquals(accommodationId, product.getAccommodation().getId());
//        assertEquals(1, response.getProductResponseList().size());
//        assertEquals(2, response.getProductResponseList().get(0).getStandardNumber());
//    }
//
//    @Test
//    void getProductDetail() {
//        ProductImageResponse productImageResponse = createProductImageResponse();
//        ProductOptionResponse productOptionResponse = createProductOptionResponse();
//        Accommodation accommodation = new Accommodation();
//        AccommodationOption accommodationOption = createAccommodationOption(accommodation);
//        AccommodationImage accommodationImage = createAccommodationImage(accommodation);
//        Product product = new Product();
//        List<Product> productList = new ArrayList<>();
//        ProductInfoPerNight productInfoPerNight = new ProductInfoPerNight();
//        List<ProductInfoPerNight> productInfoPerNightList = new ArrayList<>();
//        accommodation = createAccommodation(accommodationOption, accommodationImage, productList);
//        ProductOption productOption = createProductOption();
//        ProductImage productImage = createProductImage(product, accommodation);
//        productInfoPerNight = createProductInfoPerNight(product, accommodation);
//        productInfoPerNightList.add(productInfoPerNight);
//        product = createProduct(accommodation, productImage, productInfoPerNightList, productOption);
//        productList.add(product);
//        accommodation = createAccommodation(accommodationOption, accommodationImage, productList);
//
//        when(accommodationRepository
//            .findById(accommodationId)).thenReturn(Optional.of(accommodation));
//        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
//        when(productInfoPerNightRepository
//            .existsByProductIdAndDate(eq(productId), any(LocalDate.class))).thenReturn(true);
//        when(productInfoPerNightRepository.findMinCountByProductIdAndDateRange(eq(productId), eq(checkInDate), eq(checkOutDate)))
//            .thenReturn(productInfoPerNight.getPrice());
//        ProductDetailResponse response
//            = createProductDetailResponse(product,productImageResponse, productOptionResponse);
//
//        assertNotNull(response);
//        assertEquals(productList.get(0).getName(), response.getName());
//        assertEquals(200000, response.getTotalPrice());
//    }
//}