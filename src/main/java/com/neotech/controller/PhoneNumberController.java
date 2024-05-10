package com.neotech.controller;

import com.neotech.exception.CountryNotFoundException;
import com.neotech.exception.PhoneNumberNotValidException;
import com.neotech.model.CountryPhoneCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface PhoneNumberController {

    @Operation(summary = "Get countries by phone number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found country by phone number",
                    content = { @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CountryPhoneCode.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid phone number provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No country matches provided phone number",
                    content = @Content) })
    ResponseEntity<Set<String>> getCountriesByPhoneNumber(@RequestParam String phoneNumber) throws CountryNotFoundException, PhoneNumberNotValidException;
}
