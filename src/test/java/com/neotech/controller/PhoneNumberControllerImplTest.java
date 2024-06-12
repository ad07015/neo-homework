package com.neotech.controller;

import com.neotech.consumer.CountryCodeWikiConsumer;
import com.neotech.entity.CountryPhoneCode;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.*;
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
    private CountryPhoneCodeService service;

    @MockBean
    private CountryCodeWikiConsumer consumer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenPhoneNumberLatvian_expectLatvia() throws Exception {
        // set up
        when(service.findStartingWith("3")).thenReturn(Set.of(new CountryPhoneCode(1, "Latvia", "371")));

        var validLatvianPhoneNumber = "37129667232";

        var requestBuilder = get(NEO_COUNTRY_BY_PHONE_NUMBER_PATH)
                .param(PHONE_NUMBER_PARAM_NAME, validLatvianPhoneNumber);

        // perform
        var result = this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();

        // verify
        assertTrue(content.contains("Latvia"));
    }

    @Test
    void whenPhoneNumberCodeNotFound_expectNotFound() {
        // set up
        var nonExistingPhoneNumber = "9999999999";

        var requestBuilder = get(NEO_COUNTRY_BY_PHONE_NUMBER_PATH)
                .param(PHONE_NUMBER_PARAM_NAME, nonExistingPhoneNumber);

        // perform
        var servletException = assertThrows(ServletException.class, () ->
                this.mockMvc.perform(requestBuilder)
                        .andExpect(status().isNotFound())
                        .andReturn());

        // verify
        assertTrue(servletException.getMessage().contains("CountryNotFoundException"));
    }

    @Test
    void whenPhoneNumberNotANumber_expectNotFound() {
        // set up
        var notANumberPhoneNumber = "text";

        var requestBuilder = get(NEO_COUNTRY_BY_PHONE_NUMBER_PATH)
                .param(PHONE_NUMBER_PARAM_NAME, notANumberPhoneNumber);

        // perform
        var servletException = assertThrows(ServletException.class, () ->
                this.mockMvc.perform(requestBuilder)
                        .andExpect(status().isBadRequest())
                        .andReturn());

        // verify
        assertTrue(servletException.getMessage().contains("PhoneNumberNotValidException"));
    }

    @ParameterizedTest
    @MethodSource("provideExpectedCountriesByPhoneNumber")
    void whenValidPhoneNumber_shouldReturnListOfMatchingCountries(String phoneNumber, List<String> expectedCountries) throws Exception {
        // set up
        when(service.findStartingWith(any())).thenReturn(Set.of(
                new CountryPhoneCode(1, "Canada", "1"),
                new CountryPhoneCode(1, "United States", "1"),
                new CountryPhoneCode(1, "Bahamas", "1242"),
                new CountryPhoneCode(1, "Russia", "7"),
                new CountryPhoneCode(1, "Kazakhstan", "76"),
                new CountryPhoneCode(1, "Kazakhstan", "77"),
                new CountryPhoneCode(1, "Latvia", "371")
        ));

        var requestBuilder = get(NEO_COUNTRY_BY_PHONE_NUMBER_PATH)
                .param(PHONE_NUMBER_PARAM_NAME, phoneNumber);

        // perform
        var result = this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
        var responseContent = result.getResponse().getContentAsString();
        var matchedCountries = Arrays.stream(
                responseContent.substring(1, responseContent.length() - 1)
                        .split(","))
                .map(value -> value.substring(1, value.length() - 1)) // remove quotes
                .collect(toSet());

        // verify
        assertEquals(expectedCountries.size(), matchedCountries.size());
        for (String country : expectedCountries) {
            assertTrue(responseContent.contains(country));
        }
    }

    private static Stream<Arguments> provideExpectedCountriesByPhoneNumber() {
        return Stream.of(
                Arguments.of("12423222931", List.of("Bahamas")),
                Arguments.of("11165384765", List.of("United States", "Canada")),
                Arguments.of("71423423412", List.of("Russia")),
                Arguments.of("77112227231", List.of("Kazakhstan")),
                Arguments.of("37129667232", List.of("Latvia"))
        );
    }
}