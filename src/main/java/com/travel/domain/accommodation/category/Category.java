package com.travel.domain.accommodation.category;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum Category {
    HOTEL(1, "호텔"),
    RESORT(2, "리조트"),
    PENSION(3, "펜션"),
    MOTEL(4, "모텔"),
    GUESTHOUSE(5, "게스트하우스"),
    CAMPING(6, "캠핑/글램핑"),
    PET(7, "애견동반숙소");

    private final Integer id;
    private final String name;

    public static String fromId(Integer id) {
        return Arrays.stream(values())
            .filter(value -> value.getId().equals(id))
            .findAny()
            .map(Category::getName)
            .orElse(null);
    }
}
