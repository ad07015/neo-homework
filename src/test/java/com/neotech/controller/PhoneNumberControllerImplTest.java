package com.neotech.controller;

import com.neotech.consumer.CountryCodeWikiConsumer;
import com.neotech.exception.CountryNotFoundException;
import com.neotech.exception.PhoneNumberNotValidException;
import com.neotech.repository.CountryPhoneCodeRepository;
import com.neotech.service.CountryPhoneCodeService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = PhoneNumberControllerImpl.class)
@ContextConfiguration(classes = PhoneNumberControllerImpl.class)
@Import(PhoneNumberControllerImpl.class)
@TestPropertySource("/application-test.yml")
class PhoneNumberControllerImplTest {

    public static final String PHONE_NUMBER_PARAM_NAME = "phoneNumber";
    public static final String NEO_COUNTRY_BY_PHONE_NUMBER_PATH = "/neo/country-by-phone-number";

    @MockBean
    private CountryPhoneCodeRepository repository;
    @MockBean
    private CountryCodeWikiConsumer consumer;
    @MockBean
    private CountryPhoneCodeService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenPhoneNumberLatvian_expectLatvia() throws Exception {
        // set up
        var validLatvianPhoneNumber = "37123456789";
        when(service.findMatchedCountries(validLatvianPhoneNumber)).thenReturn(Set.of("Latvia"));

        // perform
        var mvcResult = mockMvc.perform(get(NEO_COUNTRY_BY_PHONE_NUMBER_PATH)
                .param(PHONE_NUMBER_PARAM_NAME, validLatvianPhoneNumber))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // verify
        assertThat(mvcResult, containsString("Latvia"));
    }

    @Test
    void whenPhoneNumberCodeNotFound_expectNotFound() {
        // set up
        var nonExistentPhoneNumber = "9999999999";
        when(service.findMatchedCountries(any())).thenThrow(new CountryNotFoundException(CountryNotFoundException.MESSAGE));

        var requestBuilder = get(NEO_COUNTRY_BY_PHONE_NUMBER_PATH)
                .param(PHONE_NUMBER_PARAM_NAME, nonExistentPhoneNumber);

        // perform
        var servletException = assertThrows(ServletException.class, () ->
                this.mockMvc.perform(requestBuilder)
        );
        var cause = servletException.getCause();

        // verify
        assertThat(cause.getClass(), is(CountryNotFoundException.class));
        assertThat(cause.getMessage(), is(equalTo(CountryNotFoundException.MESSAGE)));
    }

    @Test
    void whenPhoneNumberNotANumber_expectBadRequest() {
        // set up
        var notANumberPhoneNumber = "text";

        var requestBuilder = get(NEO_COUNTRY_BY_PHONE_NUMBER_PATH)
                .param(PHONE_NUMBER_PARAM_NAME, notANumberPhoneNumber);

        // perform
        var servletException = assertThrows(ServletException.class, () ->
            this.mockMvc.perform(requestBuilder)
        );
        var cause = servletException.getCause();

        // verify
        assertThat(cause.getClass(), is(PhoneNumberNotValidException.class));
        assertThat(cause.getMessage(), is(equalTo(PhoneNumberNotValidException.MESSAGE)));
    }

    @ParameterizedTest
    @MethodSource("provideExpectedCountriesByPhoneNumber")
    void whenValidPhoneNumber_shouldReturnListOfMatchingCountries(String phoneNumber, Set<String> expectedCountries) throws Exception {
        // set up
        when(service.findMatchedCountries(any())).thenReturn(expectedCountries);

        // perform
        String mvcResult = mockMvc.perform(get(NEO_COUNTRY_BY_PHONE_NUMBER_PATH)
                .param(PHONE_NUMBER_PARAM_NAME, phoneNumber))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String[] matchedCountriesArray = removeSurrounding(mvcResult).split(",");
        Set<String> matchedCountries = Arrays.stream(matchedCountriesArray)
                .map(this::removeSurrounding)
                .collect(toSet());

        // verify
        assertThat(matchedCountries.size(), is(equalTo(expectedCountries.size())));
        for (String country : expectedCountries) {
            assertThat(matchedCountries, hasItems(country));
        }
    }

    private static Stream<Arguments> provideExpectedCountriesByPhoneNumber() {
        return Stream.of(
                Arguments.of("12423222931", Set.of("Bahamas")),
                Arguments.of("11165384765", Set.of("United States", "Canada")),
                Arguments.of("71423423412", Set.of("Russia")),
                Arguments.of("77112227231", Set.of("Kazakhstan")),
                Arguments.of("37123456789", Set.of("Latvia"))
        );
    }

    private String removeSurrounding(String source) {
        return source.substring(1, source.length() - 1);
    }
}