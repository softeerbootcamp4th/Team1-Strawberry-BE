package com.hyundai.softeer.backend.domain.car.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ModelLineConvertor implements AttributeConverter<ModelLine, Integer>{
    @Override
    public Integer convertToDatabaseColumn(ModelLine attribute) {
        return attribute.getCode();
    }

    @Override
    public ModelLine convertToEntityAttribute(Integer dbData) {
        return ModelLine.of(dbData);
    }
}
