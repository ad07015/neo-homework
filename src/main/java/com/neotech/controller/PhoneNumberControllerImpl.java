package com.neotech.controller;

import com.neotech.consumer.CountryCodeWikiConsumer;
import com.neotech.exception.CountryNotFoundException;
import com.neotech.exception.PhoneNumberNotValidException;
import com.neotech.model.CountryPhoneCode;
import com.neotech.service.CountryPhoneCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/neo")
public class PhoneNumberControllerImpl implements PhoneNumberController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneNumberControllerImpl.class);
    private final CountryPhoneCodeService countryPhoneCodeService;

    public PhoneNumberControllerImpl(CountryPhoneCodeService countryPhoneCodeService) {
        this.countryPhoneCodeService = countryPhoneCodeService;
    }

    @Override
    @GetMapping(value = "/country-by-phone-number", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<String>> getCountriesByPhoneNumber(String phoneNumber) throws CountryNotFoundException, PhoneNumberNotValidException, IOException {

        if (!StringUtils.isNumeric(phoneNumber)) {
            throw new PhoneNumberNotValidException(PhoneNumberNotValidException.MESSAGE);
        }

        var countryPhoneCodes = countryPhoneCodeService.extractCountryCodesFromWiki();
        countryPhoneCodeService.persistCountryCodes(countryPhoneCodes);

        Set<CountryPhoneCode> allCodes = countryPhoneCodeService.findStartingWith(phoneNumber.substring(0, 1));
        LOGGER.info("Retrieved codes count: " + allCodes.size());

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
            throw new CountryNotFoundException(CountryNotFoundException.MESSAGE);
        }
        return new ResponseEntity<>(matchingCodes, HttpStatus.OK);
    }
}
