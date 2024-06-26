package com.travel.global.security.provider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.AuthenticatedPrincipal;

@JsonIgnoreProperties({"name"})
public record UserPrincipal(String username, String email) implements AuthenticatedPrincipal {

    public UserPrincipal(@JsonProperty("username") String username, @JsonProperty("email") String email) {
        this.username = username;
        this.email = email;
    }

    @Override
    public String getName() {
        return this.email;
    }
}