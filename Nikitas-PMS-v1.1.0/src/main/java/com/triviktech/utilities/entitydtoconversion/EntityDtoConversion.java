package com.triviktech.utilities.entitydtoconversion;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EntityDtoConversion {

    private final ModelMapper modelMapper;

    public EntityDtoConversion(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <E, D> D entityToDtoConversion(E entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public <D, E> E dtoToEntityConversion(D dtoClass , Class<E> entity) {
        return modelMapper.map(dtoClass, entity);
    }
}
