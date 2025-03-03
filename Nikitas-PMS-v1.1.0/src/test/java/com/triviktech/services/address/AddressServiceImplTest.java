package com.triviktech.services.address;

import com.triviktech.entities.address.Country;
import com.triviktech.entities.address.Location;
import com.triviktech.entities.address.State;
import com.triviktech.exception.address.CountryNotFoundException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private StateRepository stateRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerCountry_ShouldReturnSuccessResponse() {
        CountryRequestDto requestDto = new CountryRequestDto();
        requestDto.setName("India");

        Country savedCountry = new Country();
        savedCountry.setName("India");

        when(countryRepository.save(any(Country.class))).thenReturn(savedCountry);

        CountryResponseDto response = addressService.registerCountry(requestDto);

        assertNotNull(response);
        assertEquals("India", response.getName());
        assertEquals(HttpStatus.CREATED, response.getStatus());
        verify(countryRepository, times(1)).save(any(Country.class));
    }

    @Test
    void registerState_ShouldReturnSuccessResponse() {
        StateRequestDto requestDto = new StateRequestDto();
        requestDto.setName("Karnataka");
        requestDto.setCountryId(1L);

        Country country = new Country();
        country.setCountryId(1L);
        country.setName("India");

        State savedState = new State();
        savedState.setName("Karnataka");
        savedState.setCountry(country);

        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(stateRepository.save(any(State.class))).thenReturn(savedState);

        StateResponseDto response = addressService.registerState(requestDto);

        assertNotNull(response);
        assertEquals("Karnataka", response.getName());
        assertEquals("India", response.getCountry().getName());
        assertEquals(HttpStatus.CREATED, response.getStatus());
        verify(countryRepository, times(1)).findById(1L);
        verify(stateRepository, times(1)).save(any(State.class));
    }

    @Test
    void registerState_ShouldThrowCountryNotFoundException() {
        StateRequestDto requestDto = new StateRequestDto();
        requestDto.setName("Karnataka");
        requestDto.setCountryId(1L);

        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CountryNotFoundException.class, () -> addressService.registerState(requestDto));
        verify(countryRepository, times(1)).findById(1L);
        verify(stateRepository, never()).save(any(State.class));
    }

    @Test
    void registerLocation_ShouldReturnSuccessResponse() {
        LocationRequestDto requestDto = new LocationRequestDto();
        requestDto.setName("Bangalore");
        requestDto.setZipCode("560001");
        requestDto.setStateId(1L);

        Country country = new Country();
        country.setName("India");

        State state = new State();
        state.setName("Karnataka");
        state.setCountry(country);

        Location savedLocation = new Location();
        savedLocation.setName("Bangalore");
        savedLocation.setZipCode("560001");
        savedLocation.setState(state);

        when(stateRepository.findById(1L)).thenReturn(Optional.of(state));
        when(locationRepository.save(any(Location.class))).thenReturn(savedLocation);

        LocationResponseDto response = addressService.registerLocation(requestDto);

        assertNotNull(response);
        assertEquals("Bangalore", response.getName());
        assertEquals("560001", response.getZipCode());
        assertEquals("Karnataka", response.getState().getName());
        assertEquals("India", response.getState().getCountry().getName());
        assertEquals(HttpStatus.CREATED, response.getStatus());
        verify(stateRepository, times(1)).findById(1L);
        verify(locationRepository, times(1)).save(any(Location.class));
    }

    @Test
    void registerLocation_ShouldThrowStateNotFoundException() {
        LocationRequestDto requestDto = new LocationRequestDto();
        requestDto.setName("Bangalore");
        requestDto.setZipCode("560001");
        requestDto.setStateId(1L);

        when(stateRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(StateNotFoundException.class, () -> addressService.registerLocation(requestDto));
        verify(stateRepository, times(1)).findById(1L);
        verify(locationRepository, never()).save(any(Location.class));
    }
}
