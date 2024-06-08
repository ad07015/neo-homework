package com.neotech.service;

import com.neotech.consumer.CountryCodeWikiConsumer;
import com.neotech.model.CountryPhoneCode;
import com.neotech.repository.CountryPhoneCodeRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CountryPhoneCodeService {

    private final CountryCodeWikiConsumer countryCodeWikiConsumer;
    private final CountryPhoneCodeRepository countryPhoneCodeRepository;

    public CountryPhoneCodeService(CountryCodeWikiConsumer countryCodeWikiConsumer, CountryPhoneCodeRepository countryPhoneCodeRepository) {
        this.countryCodeWikiConsumer = countryCodeWikiConsumer;
        this.countryPhoneCodeRepository = countryPhoneCodeRepository;
    }

    public List<CountryPhoneCode> findAll() {
        return countryPhoneCodeRepository.findAll();
    }

    public List<CountryPhoneCode> extractCountryCodesFromWiki() throws IOException {
        Map<String, String> countryToCountryCodeMap = countryCodeWikiConsumer.extractCountryCodesFromWiki();
        var countryCodes = new ArrayList<CountryPhoneCode>();
        countryToCountryCodeMap.forEach((country, code) -> {
            countryCodes.add(new CountryPhoneCode(null, country, code));
        });
        return countryCodes;
    }

    public void persistCountryCodes(List<CountryPhoneCode> countryPhoneCodes) {
        countryPhoneCodeRepository.saveAll(countryPhoneCodes);
    }
}
