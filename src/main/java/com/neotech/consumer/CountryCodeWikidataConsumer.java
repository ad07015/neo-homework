package com.neotech.consumer;

import com.neotech.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@Service
public class CountryCodeWikidataConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CountryCodeWikidataConsumer.class);

    private static final String WIKIPEDIA_LIST_OF_COUNTRY_CALLING_CODES_URL = "https://en.wikipedia.org/wiki/List_of_country_calling_codes#Alphabetical_order";

    public Map<String, String> getCountryCodes() throws IOException {
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
            resultMap.forEach((key, val) -> logger.debug(String.format("Country: %s, Phone code: %s%n", key, val)));
            return resultMap;
        } else {
            return new HashMap<>();
        }
    }
}
