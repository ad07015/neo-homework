package com.neotech.service;

import com.neotech.consumer.CountryCodeWikiConsumer;
import com.neotech.entity.CountryPhoneCode;
import com.neotech.repository.CountryPhoneCodeRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class CountryPhoneCodeService {

    private final CountryCodeWikiConsumer countryCodeWikiConsumer;
    private final CountryPhoneCodeRepository countryPhoneCodeRepository;

    public CountryPhoneCodeService(CountryCodeWikiConsumer countryCodeWikiConsumer, CountryPhoneCodeRepository countryPhoneCodeRepository) {
        this.countryCodeWikiConsumer = countryCodeWikiConsumer;
        this.countryPhoneCodeRepository = countryPhoneCodeRepository;
    }

    public Set<CountryPhoneCode> findStartingWith(String firstDigit) {
        return new HashSet<>(countryPhoneCodeRepository.findByCodeStartingWith(firstDigit));
    }

    public Set<CountryPhoneCode> extractCountryCodesFromWiki() throws IOException {
        return countryCodeWikiConsumer.extractCountryCodesFromWiki()
                .entrySet().stream()
                .map(entry -> new CountryPhoneCode(null, entry.getKey(), entry.getValue()))
                .collect(toSet());
    }

    public void persistCountryCodes(Set<CountryPhoneCode> countryPhoneCodes) {
        countryPhoneCodeRepository.saveAll(countryPhoneCodes);
    }
}
