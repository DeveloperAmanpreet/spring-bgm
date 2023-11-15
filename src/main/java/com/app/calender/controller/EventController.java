package com.app.calender.controller;

import com.app.calender.dto.EventDto;
import com.app.calender.service.EventService;
import com.app.common.user.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping(Constants.CALENDER.CALENDER_EVENT)
@Slf4j
public class EventController {

  private final EventService eventService;

  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  public ResponseEntity<EventDto> findByIdPrivate(@PathVariable Long id, Principal principal) {
    Optional<EventDto> cashCardOptional = eventService.getById(id);
    return cashCardOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventDto> findByIdAndUser(@PathVariable Long id, Principal principal) {
    Optional<EventDto> cashCardOptional = eventService.getByIdAndUser(id, principal.getName());
    return cashCardOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<EventDto>> findAllByIdAndUser(Pageable pageable, Principal principal) throws Exception {
    log.info("Started findAllByIdAndUser");
    Page<EventDto> page  = eventService.getAllEventsByUser(pageable, principal.getName());
    return ResponseEntity.ok(page.getContent());
  }

  @PostMapping
  private ResponseEntity<Void> createEvent(
      @RequestBody EventDto newEventDto,
      UriComponentsBuilder ucb,
      Principal principal) throws Exception {
    newEventDto.setOwnerUsername(principal.getName());
    EventDto savedEventDto = eventService.save(newEventDto);
    URI locationOfNewCashCard = ucb
        .path(Constants.CALENDER.CALENDER_EVENT + "/{id}")
        .buildAndExpand(savedEventDto.getId())
        .toUri();
    return ResponseEntity.created(locationOfNewCashCard).build();
  }
}