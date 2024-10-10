package com.user_management_API.automation.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class RequestEntity {

    /** The url. */
    private String url;

    /** The method. */
    private String method;

    /** The parameters. */
    private Map<String, ?> parameters;

    /** The parametersList. */
    private ArrayList<Map<String, ?>> parametersList;
}
