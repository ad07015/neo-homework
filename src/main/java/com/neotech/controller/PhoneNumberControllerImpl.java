package com.neotech.controller;

import com.neotech.exception.CountryNotFoundException;
import com.neotech.exception.PhoneNumberNotValidException;
import com.neotech.service.CountryPhoneCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/neo")
public class PhoneNumberControllerImpl implements PhoneNumberController {

    private final CountryPhoneCodeService service;

    public PhoneNumberControllerImpl(CountryPhoneCodeService service) {
        this.service = service;
    }

    @Override
    @GetMapping(value = "/country-by-phone-number", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<String>> getCountriesByPhoneNumber(String phoneNumber)
            throws CountryNotFoundException, PhoneNumberNotValidException {

        if (StringUtils.isBlank(phoneNumber) || !StringUtils.isNumeric(phoneNumber)) {
            throw new PhoneNumberNotValidException(PhoneNumberNotValidException.MESSAGE);
        }

        return new ResponseEntity<>(service.findMatchedCountries(phoneNumber), HttpStatus.OK);
    }
}
