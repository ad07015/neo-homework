package com.neotech.consumer;

import com.neotech.domain.*;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
public class CountryCodeWikidataConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CountryCodeWikidataConsumer.class);
    private static final String WIKIPEDIA_API_QUERY_LIST_OF_COUNTRY_CALLING_CODES = "https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=json&titles=List_of_country_calling_codes&rvslots=main";

    public ResponseEntity<WikidataCountryCodeResponse> extractCountryCodes() throws EngineException, LinkTargetException {
        var restTemplate = new RestTemplate();
        var wikiText = restTemplate.getForObject(
                WIKIPEDIA_API_QUERY_LIST_OF_COUNTRY_CALLING_CODES, String.class);

        var convertedWikiText = convertWikiText("List of country calling codes", wikiText, 10000000);
        logger.debug(format("Converted wikiText: %s", convertedWikiText));

        var response = new WikidataCountryCodeResponse(new Results(List.of(new Binding(new CC("CCValue"), new ItemLabel("itemLabelValue")))));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public String convertWikiText(String title, String wikiText, int maxLineLength) throws LinkTargetException, EngineException {
        // Set-up a simple wiki configuration
        WikiConfig config = DefaultConfigEnWp.generate();
        // Instantiate a compiler for wiki pages
        WtEngineImpl engine = new WtEngineImpl(config);
        // Retrieve a page
        PageTitle pageTitle = PageTitle.make(config, title);
        PageId pageId = new PageId(pageTitle, -1);
        // Compile the retrieved page
        EngProcessedPage cp = engine.postprocess(pageId, wikiText, null);
        TextConverter p = new TextConverter(config, maxLineLength);
        return (String)p.go(cp.getPage());
    }

    private void printWikiDocument(WikibaseDataFetcher wbdf, String pageId) throws IOException, MediaWikiApiErrorException {
        var page = wbdf.getEntityDocument(pageId);
        if (page instanceof ItemDocument wikiDocument) {
            logger.debug("-------------------------------------------------------------");
            logger.debug(format("Wiki document entity ID: %s", wikiDocument.getEntityId().getId()));
            wikiDocument.getLabels().forEach((key, value) -> logger.debug(format("Label key: %s, Label value: %s", key, value)));
            wikiDocument.getAliases().forEach((key, value) -> logger.debug(format("Alias key: %s, Alias value: %s", key, value)));
            wikiDocument.getStatementGroups().forEach(sg -> logger.debug(format("Statement group: %s", sg.toString())));
            wikiDocument.getSiteLinks().forEach((key, value) -> logger.debug(format("Site link key: %s, Site link value: %s", key, value)));
            wikiDocument.getAllStatements().forEachRemaining(statement -> {
                logger.debug("---------------");
                logger.debug(format("Subject Id: %s", statement.getSubject().getId()));
                logger.debug(format("Subject entity type: %s", statement.getSubject().getEntityType()));
                logger.debug(format("Subject IRI: %s", statement.getSubject().getIri()));
                logger.debug(format("Subject site IRI: %s", statement.getSubject().getSiteIri()));
                logger.debug("---");
                logger.debug(format("Statement main SNAK: %s", statement.getMainSnak()));
                logger.debug(format("Statement value: %s", statement.getValue()));
                logger.debug("---------------");
            });
            wikiDocument.getDescriptions().forEach((key, value) -> logger.debug(format("Description key: %s, description value: %s", key, value)));
        }
    }
}
