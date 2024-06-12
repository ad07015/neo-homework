package com.neotech.extract;

import com.neotech.consumer.CountryCodeWikiConsumer;
import com.neotech.entity.CountryPhoneCode;
import com.neotech.service.CountryPhoneCodeService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.util.stream.Collectors.toSet;

@Component
public class WikipediaCountryPhoneCodeExtractor {

    private final CountryPhoneCodeService service;

    public WikipediaCountryPhoneCodeExtractor(CountryPhoneCodeService service) {
        this.service = service;
    }

    @PostConstruct
    public void extractCountryCodesFromWikipedia() throws IOException {
        service.clearDatabase();
        var countryPhoneCodes = CountryCodeWikiConsumer.extractCountryCodesFromWiki()
                .stream()
                .map(entry -> new CountryPhoneCode(null, entry.country(), entry.code()))
                .collect(toSet());
        service.persistCountryCodes(countryPhoneCodes);
    }
}
