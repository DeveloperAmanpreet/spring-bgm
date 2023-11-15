package com.app.config.security;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

@JsonSerialize
@JsonDeserialize
@Getter
@Setter
public class AuthResponseDTO {
  private String accessToken;
}
