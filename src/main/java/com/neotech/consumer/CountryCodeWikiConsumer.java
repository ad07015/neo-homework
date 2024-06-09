package com.neotech.consumer;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CountryCodeWikiConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryCodeWikiConsumer.class);
    private static final String WIKIPEDIA_LIST_OF_COUNTRY_CALLING_CODES_URL = "https://en.wikipedia.org/wiki/List_of_country_calling_codes#Alphabetical_order";

    public Map<String, String> extractCountryCodesFromWiki() throws IOException {
        var document = Jsoup.connect(WIKIPEDIA_LIST_OF_COUNTRY_CALLING_CODES_URL)
                .get();
        var tableOpt = Optional.ofNullable(document.select("table.wikitable.sortable").first());
        if (tableOpt.isPresent()) {
            var table = tableOpt.get();
            var resultMap = new HashMap<String, String>();
            for (var row : table.select("tr").subList(1, table.select("tr").size())) {
                var columns = row.select("td, th");
                var countryName = columns.get(0).text();
                var phoneCode = columns.get(1).text();
                resultMap.put(countryName, phoneCode);
            }
            return resultMap;
        } else {
            return new HashMap<>();
        }
    }
}
