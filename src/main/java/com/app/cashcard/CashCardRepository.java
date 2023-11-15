package com.app.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.stereotype.Repository;

@Repository
@Table("cash_card")
public interface CashCardRepository extends JpaRepository<CashCard, Long> {
  CashCard findByIdAndOwner(Long id, String owner);

  Page<CashCard> findByOwner(String owner, PageRequest pageRequest);
  boolean existsByIdAndOwner(Long id, String owner);
}
