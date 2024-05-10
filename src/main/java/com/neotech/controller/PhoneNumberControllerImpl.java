package com.neotech.controller;

import com.neotech.exception.CountryNotFoundException;
import com.neotech.exception.PhoneNumberNotValidException;
import com.neotech.repository.CountryPhoneCodeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/neo")
public class PhoneNumberControllerImpl implements PhoneNumberController {

    private final CountryPhoneCodeRepository countryPhoneCodeRepository;

    public PhoneNumberControllerImpl(CountryPhoneCodeRepository countryPhoneCodeRepository) {
        this.countryPhoneCodeRepository = countryPhoneCodeRepository;
    }

    @Override
    @GetMapping(value = "/country-by-phone-number", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<String>> getCountriesByPhoneNumber(String phoneNumber) throws CountryNotFoundException, PhoneNumberNotValidException {
        if (!StringUtils.isNumeric(phoneNumber)) {
            throw new PhoneNumberNotValidException("Invalid phone number provided");
        }

        var allCodes = countryPhoneCodeRepository.findAll();
        var matchingCodes = allCodes.stream()
                .map(code -> {
                    if (phoneNumber.startsWith(code.getCode())) {
                        return code.getCountry();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(toSet());

        if (matchingCodes.isEmpty()) {
            throw new CountryNotFoundException("No country matches provided phone number");
        }
        return new ResponseEntity<>(matchingCodes, HttpStatus.OK);
    }
}