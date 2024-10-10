package com.user_management_API.automation.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class User {

    private String url;
    private String apikey;
}
