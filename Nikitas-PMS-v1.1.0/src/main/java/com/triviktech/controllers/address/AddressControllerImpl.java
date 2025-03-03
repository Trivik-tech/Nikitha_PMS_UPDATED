package com.triviktech.controllers.address;

import com.triviktech.payloads.request.address.CountryRequestDto;
import com.triviktech.payloads.request.address.LocationRequestDto;
import com.triviktech.payloads.request.address.StateRequestDto;
import com.triviktech.payloads.response.address.CountryResponseDto;
import com.triviktech.payloads.response.address.StateResponseDto;
import com.triviktech.utilities.validation.ValidationMessage;
import com.triviktech.services.address.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class AddressControllerImpl implements AddressController{

    private final AddressService addressService;


    public AddressControllerImpl(AddressService addressService) {
        this.addressService = addressService;

    }


    @Override
    public ResponseEntity<?> registerCountry(CountryRequestDto countryRequestDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            ValidationMessage validationMessage=new ValidationMessage();

            validationMessage.setMessage(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            validationMessage.setStatus(HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(validationMessage,HttpStatus.NOT_ACCEPTABLE);

        }

        return new ResponseEntity<>(addressService.registerCountry(countryRequestDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> registerState(StateRequestDto stateRequestDto,BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            ValidationMessage validationMessage=new ValidationMessage();
            validationMessage.setMessage(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            validationMessage.setStatus(HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(validationMessage,HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(addressService.registerState(stateRequestDto),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> registerLocation(LocationRequestDto locationRequestDto,BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            ValidationMessage validationMessage=new ValidationMessage();
            validationMessage.setMessage(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            validationMessage.setStatus(HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(validationMessage,HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(addressService.registerLocation(locationRequestDto),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<StateResponseDto>> listOfStates() {
        return ResponseEntity.ok(addressService.listOfStates());
    }

    @Override
    public ResponseEntity<List<CountryResponseDto>> listOfCountry() {
        return ResponseEntity.ok(addressService.listOfCountry());
    }

    @Override
    public ResponseEntity<?> updateCountry(CountryRequestDto countryRequestDto, long countryId,BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            ValidationMessage validationMessage=new ValidationMessage();
            validationMessage.setMessage(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            validationMessage.setStatus(HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(validationMessage,HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(addressService.updateCountry(countryRequestDto, countryId));
    }

    @Override
    public ResponseEntity<?> updateState(StateRequestDto stateRequestDto, long stateId, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            ValidationMessage validationMessage=new ValidationMessage();
            validationMessage.setMessage(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            validationMessage.setStatus(HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(validationMessage,HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(addressService.updateState(stateRequestDto,stateId));
    }

    @Override
    public ResponseEntity<?> updateLocation(LocationRequestDto locationRequestDto, long locationId, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            ValidationMessage validationMessage=new ValidationMessage();
            validationMessage.setMessage(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            validationMessage.setStatus(HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(validationMessage,HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(addressService.updateLocation(locationRequestDto,locationId));
    }

    @Override
    public ResponseEntity<CountryResponseDto> getCountry(long countryId) {
        return ResponseEntity.ok(addressService.findCountry(countryId));
    }

    @Override
    public ResponseEntity<StateResponseDto> getState(long stateId) {
        return ResponseEntity.ok(addressService.findState(stateId));
    }
}
