package com.neotech.controller;

import com.neotech.exception.CountryNotFoundException;
import com.neotech.model.CountryPhoneCode;
import com.neotech.repository.CountryPhoneCodeRepository;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenPhoneNumberLatvian_expectLatvia() throws Exception {
        // set up
        when(repository.findAll()).thenReturn(List.of(new CountryPhoneCode(1, "Latvia", "371")));

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
        var notANumberPhoneNumber = "asdf";

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
}