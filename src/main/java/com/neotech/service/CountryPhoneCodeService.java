package com.neotech.service;

import com.neotech.entity.CountryPhoneCode;
import com.neotech.repository.CountryPhoneCodeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class CountryPhoneCodeService {

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
