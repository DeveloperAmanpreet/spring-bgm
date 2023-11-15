package com.app.calender.store.sql;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event")
public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;

  String name;

  Long owner;

  @Column(name = "start_time")
  Instant startTime;
}
