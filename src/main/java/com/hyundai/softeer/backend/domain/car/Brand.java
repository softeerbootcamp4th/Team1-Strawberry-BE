package com.hyundai.softeer.backend.domain.car;

public enum Brand {
    HYUNDAI(10),
    KIA(20),
    GENESIS(30);

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
