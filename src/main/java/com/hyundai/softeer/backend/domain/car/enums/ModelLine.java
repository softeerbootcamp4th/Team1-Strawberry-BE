package com.hyundai.softeer.backend.domain.car.enums;

public enum ModelLine {
    GASOLINE(0),
    ELECTRIC(1),
    HYBRID(2),
    DIESEL(3),
    HYDROGEN(4);

    private int code;

    ModelLine(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ModelLine of(int brandValue) {
        for (ModelLine eachModelLine : ModelLine.values()) {
            if (eachModelLine.getCode() == brandValue) {
                return eachModelLine;
            }
        }
        throw new IllegalStateException("존재하지 않는 ModelLine 타입입니다.");
    }
}
