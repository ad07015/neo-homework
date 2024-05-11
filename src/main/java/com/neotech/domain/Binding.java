package com.neotech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Binding(CC cc, ItemLabel itemLabel) { }