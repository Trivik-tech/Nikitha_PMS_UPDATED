package com.triviktech.controllers.address;

import com.triviktech.payloads.request.address.CountryRequestDto;
import com.triviktech.payloads.request.address.LocationRequestDto;
import com.triviktech.payloads.request.address.StateRequestDto;
import com.triviktech.payloads.response.address.CountryResponseDto;
import com.triviktech.payloads.response.address.StateResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/pms/address")
public interface AddressController {

    @PostMapping("/register-country")
    ResponseEntity<?> registerCountry(@Valid @RequestBody CountryRequestDto countryRequestDto, BindingResult bindingResult);

    @PostMapping("/register-state")
    ResponseEntity<?> registerState(@Valid @RequestBody StateRequestDto stateRequestDto,BindingResult bindingResult);

    @PostMapping("/register-location")
    ResponseEntity<?> registerLocation(@Valid @RequestBody LocationRequestDto locationRequestDto,BindingResult bindingResult);

    @GetMapping("/state-list")
    ResponseEntity<List<StateResponseDto>> listOfStates();

    @GetMapping("/country-list")
    ResponseEntity<List<CountryResponseDto>> listOfCountry();

    @PutMapping("/update-country/{countryId}")
    ResponseEntity<?> updateCountry(@Valid @RequestBody CountryRequestDto countryRequestDto,@PathVariable long countryId,BindingResult bindingResult);

    @PutMapping("/update-state/{stateId}")
    ResponseEntity<?> updateState(@Valid @RequestBody StateRequestDto stateRequestDto,@PathVariable long stateId,BindingResult bindingResult);

    @PutMapping("/update-location/{locationId}")
    ResponseEntity<?> updateLocation(@Valid @RequestBody LocationRequestDto locationRequestDto,@PathVariable long locationId,BindingResult bindingResult);

    @GetMapping("/country/{countryId}")
    ResponseEntity<CountryResponseDto> getCountry(@PathVariable long countryId);

    @GetMapping("/state/{stateId}")
    ResponseEntity<StateResponseDto> getState(@PathVariable long stateId);
}
