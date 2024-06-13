package com.neotech.service;

import com.neotech.entity.CountryPhoneCode;
import com.neotech.exception.CountryNotFoundException;
import com.neotech.repository.CountryPhoneCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
public class CountryPhoneCodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryPhoneCodeService.class);

    private final CountryPhoneCodeRepository repository;

    public CountryPhoneCodeService(CountryPhoneCodeRepository repository) {
        this.repository = repository;
    }

    public void clearDatabase() {
        repository.deleteAll();
    }

    public Set<CountryPhoneCode> findStartingWith(String firstDigit) {
        return new HashSet<>(repository.findByCodeStartingWith(firstDigit));
    }

    public Set<String> findMatchedCountries(String phoneNumber) {
        var firstDigit = phoneNumber.substring(0, 1);
        var allCodes = findStartingWith(firstDigit);
        LOGGER.info("Retrieved codes count: " + allCodes.size());

        var matchingCodes = allCodes.stream()
                .filter(code -> phoneNumber.startsWith(code.getCode()))
                .collect(toSet());

        if (matchingCodes.isEmpty()) {
            throw new CountryNotFoundException(CountryNotFoundException.MESSAGE);
        }

        var maxCodeLength = Collections.max(
                matchingCodes.stream()
                        .map(countryCode -> countryCode.getCode().length())
                        .collect(toSet())
        );
        return matchingCodes.stream()
                .filter(countryCode -> countryCode.getCode().length() == maxCodeLength)
                .map(CountryPhoneCode::getCountry)
                .collect(toSet());
    }

    public void persistCountryCodes(Set<CountryPhoneCode> incomingCountryPhoneCodes) {
        var countryPhoneCodes = new ArrayList<CountryPhoneCode>();
        for (CountryPhoneCode countryPhoneCode : incomingCountryPhoneCodes) {
            var country = countryPhoneCode.getCountry();
            var code = countryPhoneCode.getCode();
            if (code.contains("(")) {
                var regionalCodes = code.substring(code.indexOf("(") + 1, code.indexOf(")"));
                var countryRegionalPhoneCodes = Stream.of(regionalCodes.split(",", -1))
                        .map(String::trim)
                        .map(regionalCode -> {
                            var countryCode = code.substring(0, code.indexOf(("(")) - 1);
                            return countryCode + regionalCode;
                        })
                        .map(countryCode -> new CountryPhoneCode(null, country, countryCode))
                        .toList();
                countryPhoneCodes.addAll(countryRegionalPhoneCodes);
            } else {
                countryPhoneCodes.add(new CountryPhoneCode(null, country, code));
            }
        }
        repository.saveAll(countryPhoneCodes);
    }
}
