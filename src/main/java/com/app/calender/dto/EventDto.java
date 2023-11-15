package com.app.calender.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@Builder
public class EventDto {
  Long id;
  String name;
  Long userid;
  String ownerUsername;
  Long owner;
}
