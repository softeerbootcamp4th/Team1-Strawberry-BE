package com.hyundai.softeer.backend.domain.car.enums;

public enum Brand {
    HYUNDAI(0),
    KIA(1),
    GENESIS(2);

    private int brandValue;

    Brand(int brandValue) {
        this.brandValue = brandValue;
    }

    public int getBrandValue() {
        return brandValue;
    }

    public static Brand of(int brandValue) {
        for (Brand eachBrand : Brand.values()) {
            if (eachBrand.getBrandValue() == brandValue) {
                return eachBrand;
            }
        }
        throw new IllegalStateException("존재하지 않는 Brand 타입입니다.");
    }
}
