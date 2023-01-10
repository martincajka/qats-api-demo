package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserResponse {
    private String id;
    private Instant createdAt;
}
