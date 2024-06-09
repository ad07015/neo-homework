package com.neotech.service;

import com.neotech.consumer.CountryCodeWikiConsumer;
import com.neotech.model.CountryPhoneCode;
import com.neotech.repository.CountryPhoneCodeRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class CountryPhoneCodeService {

    private final CountryCodeWikiConsumer countryCodeWikiConsumer;
    private final CountryPhoneCodeRepository countryPhoneCodeRepository;

    public CountryPhoneCodeService(CountryCodeWikiConsumer countryCodeWikiConsumer, CountryPhoneCodeRepository countryPhoneCodeRepository) {
        this.countryCodeWikiConsumer = countryCodeWikiConsumer;
        this.countryPhoneCodeRepository = countryPhoneCodeRepository;
    }

    public Set<CountryPhoneCode> findAll() {
        return new HashSet<>(countryPhoneCodeRepository.findAll());
    }

    public Set<CountryPhoneCode> findStartingWith(String firstDigit) {
        return new HashSet<>(countryPhoneCodeRepository.findByCodeStartingWith(firstDigit));
    }

    public Set<CountryPhoneCode> extractCountryCodesFromWiki() throws IOException {
        Map<String, String> countryToCountryCodeMap = countryCodeWikiConsumer.extractCountryCodesFromWiki();
        var countryCodes = new HashSet<CountryPhoneCode>();
        countryToCountryCodeMap.forEach((country, code) -> {
            countryCodes.add(new CountryPhoneCode(null, country, code));
        });
        return countryCodes;
    }

    public void persistCountryCodes(Set<CountryPhoneCode> countryPhoneCodes) {
        countryPhoneCodeRepository.saveAll(countryPhoneCodes);
    }
}
