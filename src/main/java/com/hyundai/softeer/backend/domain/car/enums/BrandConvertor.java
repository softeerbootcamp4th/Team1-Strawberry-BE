package com.hyundai.softeer.backend.domain.car.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BrandConvertor implements AttributeConverter<Brand, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Brand attribute) {
        return attribute.getBrandValue();
    }

    @Override
    public Brand convertToEntityAttribute(Integer dbData) {
        return Brand.of(dbData);
    }
}
