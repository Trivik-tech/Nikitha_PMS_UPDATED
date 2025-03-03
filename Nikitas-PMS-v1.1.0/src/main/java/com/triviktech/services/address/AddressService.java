package com.triviktech.services.address;

import com.triviktech.payloads.request.address.CountryRequestDto;
import com.triviktech.payloads.request.address.LocationRequestDto;
import com.triviktech.payloads.request.address.StateRequestDto;
import com.triviktech.payloads.response.address.CountryResponseDto;
import com.triviktech.payloads.response.address.LocationResponseDto;
import com.triviktech.payloads.response.address.StateResponseDto;

import java.util.List;

public interface AddressService {


    CountryResponseDto registerCountry(CountryRequestDto countryRequestDto);
    StateResponseDto registerState(StateRequestDto stateRequestDto);
    LocationResponseDto registerLocation(LocationRequestDto locationRequestDto);

    List<StateResponseDto> listOfStates();

    List<CountryResponseDto> listOfCountry();

    CountryResponseDto updateCountry(CountryRequestDto countryRequestDto,long countryId);

    StateResponseDto updateState(StateRequestDto stateRequestDto,long stateId);

    LocationResponseDto updateLocation(LocationRequestDto locationRequestDto,long locationId);

    CountryResponseDto findCountry(long countryId);
    StateResponseDto findState(long stateId);

}
