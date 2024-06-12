package com.neotech.consumer;

import com.neotech.model.PhoneCodeDTO;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CountryCodeWikiConsumer {

    private static final String WIKIPEDIA_LIST_OF_COUNTRY_CALLING_CODES_URL = "https://en.wikipedia.org/wiki/List_of_country_calling_codes#Alphabetical_order";

    public static Set<PhoneCodeDTO> extractCountryCodesFromWiki() throws IOException {
        var document = Jsoup.connect(WIKIPEDIA_LIST_OF_COUNTRY_CALLING_CODES_URL).get();
        var tableOpt = Optional.ofNullable(document.select("table.wikitable.sortable").first());
        var result = new HashSet<PhoneCodeDTO>();
        if (tableOpt.isPresent()) {
            var tableElement = tableOpt.get();
            for (var row : tableElement.select("tr").subList(1, tableElement.select("tr").size())) {
                var columns = row.select("td, th");
                var countryName = columns.get(0).text();
                var phoneCode = columns.get(1).text();
                result.add(new PhoneCodeDTO(countryName, phoneCode));
            }
        }
        return result;
    }
}
