package com.app.common.user;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonDeserialize
@JsonSerialize
@Builder
public class LoginData {
  String username;
  String password;
}
