package com.triviktech.services.address;

import com.triviktech.entities.address.Country;
import com.triviktech.entities.address.Location;
import com.triviktech.entities.address.State;
import com.triviktech.exception.address.CountryNotFoundException;
import com.triviktech.exception.address.LocationNotFoundException;
import com.triviktech.exception.address.StateNotFoundException;
import com.triviktech.payloads.request.address.CountryRequestDto;
import com.triviktech.payloads.request.address.LocationRequestDto;
import com.triviktech.payloads.request.address.StateRequestDto;
import com.triviktech.payloads.response.address.CountryResponseDto;
import com.triviktech.payloads.response.address.LocationResponseDto;
import com.triviktech.payloads.response.address.StateResponseDto;
import com.triviktech.repositories.address.CountryRepository;
import com.triviktech.repositories.address.LocationRepository;
import com.triviktech.repositories.address.StateRepository;
import com.triviktech.utilities.entitydtoconversion.EntityDtoConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final LocationRepository locationRepository;
    private final EntityDtoConversion entityDtoConversion;
    private final static String DEFAULT_COUNTRY_MESSAGE="Country not found with id: {}";
    private final static String DEFAULT_STATE_MESSAGE="State not found with id: {}";

    public AddressServiceImpl(CountryRepository countryRepository, StateRepository stateRepository, LocationRepository locationRepository, EntityDtoConversion entityDtoConversion) {
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
        this.locationRepository = locationRepository;
        this.entityDtoConversion = entityDtoConversion;
    }

    @Override
    public CountryResponseDto registerCountry(CountryRequestDto countryRequestDto) {
        Country country = entityDtoConversion.dtoToEntityConversion(countryRequestDto, Country.class);
        Country savedCountry = countryRepository.save(country);
        return entityDtoConversion.entityToDtoConversion(savedCountry, CountryResponseDto.class);
    }

    @Override
    public StateResponseDto registerState(StateRequestDto stateRequestDto) {
        Optional<Country> countryOpt = countryRepository.findById(stateRequestDto.getCountryId());
        if (countryOpt.isEmpty()) {
            logger.error(DEFAULT_COUNTRY_MESSAGE, stateRequestDto.getCountryId());
            throw new CountryNotFoundException(stateRequestDto.getCountryId());
        }

        State state = entityDtoConversion.dtoToEntityConversion(stateRequestDto, State.class);
        state.setCountry(countryOpt.get());
        State savedState = stateRepository.save(state);
        return entityDtoConversion.entityToDtoConversion(savedState, StateResponseDto.class);
    }

    @Override
    public LocationResponseDto registerLocation(LocationRequestDto locationRequestDto) {
        Optional<State> stateOpt = stateRepository.findById(locationRequestDto.getStateId());
        if (stateOpt.isEmpty()) {
            logger.error(DEFAULT_STATE_MESSAGE, locationRequestDto.getStateId());
            throw new StateNotFoundException(locationRequestDto.getStateId());
        }

        Location location = entityDtoConversion.dtoToEntityConversion(locationRequestDto, Location.class);
        location.setState(stateOpt.get());
        Location savedLocation = locationRepository.save(location);
        return entityDtoConversion.entityToDtoConversion(savedLocation, LocationResponseDto.class);
    }

    @Override
    public List<CountryResponseDto> listOfCountry() {
        return countryRepository.findAll().stream()
                .map(country -> entityDtoConversion.entityToDtoConversion(country, CountryResponseDto.class))
                .toList();
    }

    @Override
    public List<StateResponseDto> listOfStates() {
        return stateRepository.findAll().stream()
                .map(state -> entityDtoConversion.entityToDtoConversion(state, StateResponseDto.class))
                .toList();
    }

    @Override
    public CountryResponseDto updateCountry(CountryRequestDto countryRequestDto, long countryId) {
        Optional<Country> countryOpt = countryRepository.findById(countryId);
        if (countryOpt.isEmpty()) {
            logger.error(DEFAULT_COUNTRY_MESSAGE, countryId);
            throw new CountryNotFoundException(countryId);
        }

        Country country = countryOpt.get();
        country.setName(countryRequestDto.getName());
        return entityDtoConversion.entityToDtoConversion(countryRepository.save(country), CountryResponseDto.class);
    }

    @Override
    public StateResponseDto updateState(StateRequestDto stateRequestDto, long stateId) {
        Optional<State> stateOpt = stateRepository.findById(stateId);
        if (stateOpt.isEmpty()) {
            logger.error(DEFAULT_STATE_MESSAGE, stateId);
            throw new StateNotFoundException(stateId);
        }

        State state = stateOpt.get();
        state.setName(stateRequestDto.getName());
        return entityDtoConversion.entityToDtoConversion(stateRepository.save(state), StateResponseDto.class);
    }

    @Override
    public LocationResponseDto updateLocation(LocationRequestDto locationRequestDto, long locationId) {
        Optional<Location> locationOpt = locationRepository.findById(locationId);
        if (locationOpt.isEmpty()) {
            logger.error("Location not found with id: {}", locationId);
            throw new LocationNotFoundException(locationId);
        }

        Location location = locationOpt.get();
        location.setName(locationRequestDto.getName());
        return entityDtoConversion.entityToDtoConversion(locationRepository.save(location), LocationResponseDto.class);
    }

    @Override
    public CountryResponseDto findCountry(long countryId) {
        return countryRepository.findById(countryId)
                .map(country -> entityDtoConversion.entityToDtoConversion(country, CountryResponseDto.class))
                .orElseThrow(() -> {
                    logger.error(DEFAULT_COUNTRY_MESSAGE, countryId);
                    return new CountryNotFoundException(countryId);
                });
    }

    @Override
    public StateResponseDto findState(long stateId) {
        return stateRepository.findById(stateId)
                .map(state -> entityDtoConversion.entityToDtoConversion(state, StateResponseDto.class))
                .orElseThrow(() -> {
                    logger.error(DEFAULT_STATE_MESSAGE, stateId);
                    return new StateNotFoundException(stateId);
                });
    }
}
