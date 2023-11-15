package com.app.calender.repos;

import com.app.calender.store.sql.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
  Event findByIdAndOwner(Long id, Long owner);
  Page<Event> findByOwner(Long owner, Pageable pageable);
}
