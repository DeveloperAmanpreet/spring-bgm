package com.app.calender.service;

import com.app.calender.dto.EventDto;
import com.app.calender.repos.EventRepository;
import com.app.calender.store.sql.Event;
import com.app.common.user.UserDto;
import com.app.common.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
public class EventService {
  private final EventRepository eventRepository;
  private final UserService userService;

  public EventService(EventRepository eventRepository, UserService userService) {
    this.eventRepository = eventRepository;
    this.userService = userService;
  }

  public Optional<EventDto> getById(Long id) {
    Optional<Event> event = this.eventRepository.findById(id);
    EventDto eventDto = null;
    if(event.isPresent()) {
      eventDto = EventDto.builder()
          .id(event.get().getId())
          .name(event.get().getName())
          .build();
    }
    return Optional.ofNullable(eventDto);
  }

  public Optional<EventDto> getByIdAndUser(Long id, String owner) {
    log.info("Started getByIdAndUser");
    UserDto userDto = this.userService.getUserByUsername(owner);
    Optional<Event> event = Optional.ofNullable(this.eventRepository.findByIdAndOwner(id, userDto.getId()));
    EventDto eventDto = null;
    if(event.isPresent()) {
      eventDto = EventDto.builder()
          .id(event.get().getId())
          .name(event.get().getName())
          .build();
    }
    return Optional.ofNullable(eventDto);
  }

  public Page<EventDto> getAllEventsByUser(Pageable pageable, String username) throws Exception {
    log.info("Started getAllEventsByUser");
    UserDto userDto = this.userService.getUserByUsername(username);

    if(userDto == null){
      throw new Exception(username + ": user not found");
    }
    log.info("User found with id: " + userDto.getId());

    Page<Event> page = this.eventRepository.findByOwner(
        userDto.getId(),
        PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSortOr(Sort.by(Sort.Direction.ASC, "id"))
        )
    );

    return page.map(this::convertToDto);
  }

  private EventDto convertToDto(Event event) {
    return EventDto.builder().build();
  }

  public EventDto save(EventDto newEventDtoRequest) throws Exception {
    Event event = saveEvent(newEventDtoRequest);
    return EventDto.builder()
        .id(event.getId())
        .name(event.getName())
        .build();
  }

  public EventDto saveByUserid(EventDto newEventDtoRequest) throws Exception {
    Event event = saveEvent(newEventDtoRequest);
    return EventDto.builder()
        .id(event.getId())
        .name(event.getName())
        .build();
  }

  public Event saveEvent(EventDto eventDto) throws Exception {
    UserDto userDto = this.userService.getUserByUsername(eventDto.getOwnerUsername());
    if(userDto == null) {
      throw new Exception( eventDto.getOwnerUsername()+" : User not found");
    }
    Event event = new Event(eventDto.getId(), eventDto.getName(), userDto.getId(), Instant.now());
    return this.eventRepository.save(event);
  }

  Event createEventFromDto(EventDto eventDto){
    return new Event(eventDto.getId(), eventDto.getName(), eventDto.getOwner(), Instant.now());
  }
}
