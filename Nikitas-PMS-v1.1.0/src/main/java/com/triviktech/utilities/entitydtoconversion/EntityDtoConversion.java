package com.triviktech.utilities.entitydtoconversion;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Utility class for converting between Entity and DTO objects.
 *
 * Uses ModelMapper to map properties between Entity and DTO classes.
 */
@Component
public class EntityDtoConversion {

    private final ModelMapper modelMapper;

    /**
     * Constructor to initialize EntityDtoConversion with a ModelMapper instance.
     *
     * @param modelMapper the ModelMapper instance used for object mapping
     */
    public EntityDtoConversion(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Converts an entity object to its corresponding DTO object.
     *
     * @param <E>       the type of the entity
     * @param <D>       the type of the DTO
     * @param entity    the entity object to convert
     * @param dtoClass  the class type of the DTO
     * @return          the converted DTO object
     */
    public <E, D> D entityToDtoConversion(E entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    /**
     * Converts a DTO object to its corresponding entity object.
     *
     * @param <D>       the type of the DTO
     * @param <E>       the type of the entity
     * @param dtoClass  the DTO object to convert
     * @param entity    the class type of the entity
     * @return          the converted entity object
     */
    public <D, E> E dtoToEntityConversion(D dtoClass, Class<E> entity) {
        return modelMapper.map(dtoClass, entity);
    }
}
