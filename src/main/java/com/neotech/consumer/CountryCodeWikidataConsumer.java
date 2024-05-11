package com.neotech.consumer;

import com.neotech.domain.WikidataCountryCodeResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class CountryCodeWikidataConsumer {

    private static final String WIKIDATA_COUNTRY_CODE_ENDPOINT = "https://query.wikidata.org/bigdata/namespace/wdq/sparql?query=SELECT%0A%3Fcc%0A%3Falpha2%0A%3FitemLabel%0A%3Fitem%0AWHERE%0A%7B%0A%3Fitem%20wdt%3AP474%20%3Fcc%20.%0A%3Fitem%20wdt%3AP297%20%20%3Falpha2%20.%0ASERVICE%20wikibase%3Alabel%20%7B%20bd%3AserviceParam%20wikibase%3Alanguage%20%22en%2Cen%22%20%20%7D%20%20%20%20%0A%7D%0Aorder%20by%20%3Fcc&format=json";

    private final RestTemplate restTemplate;

    public CountryCodeWikidataConsumer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void extractCountryCodes() {
        WikidataCountryCodeResponse response = restTemplate.getForObject(WIKIDATA_COUNTRY_CODE_ENDPOINT, WikidataCountryCodeResponse.class);
        long bindingsCount = Optional.ofNullable(response).map(resp -> resp.results().bindings().size()).orElseThrow();
        System.out.println("Bindings count: " + bindingsCount);
    }
}
