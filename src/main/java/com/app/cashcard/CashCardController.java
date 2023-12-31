package com.app.cashcard;

import com.app.common.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(Constants.URI.CASH_CARD)
public class CashCardController {
  private CashCardRepository cashCardRepository;

  public CashCardController(CashCardRepository cashCardRepository) {
    this.cashCardRepository = cashCardRepository;
  }

  @GetMapping("/{requestedId}")
  public ResponseEntity<CashCard> findById(@PathVariable Long requestedId, Principal principal) {
    CashCard cashCard = findCashCard(requestedId, principal);
    if (cashCard != null) {
      return ResponseEntity.ok(cashCard);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb, Principal principal) {
    CashCard cashCardWithOwner = new CashCard(null, newCashCardRequest.getAmount(), principal.getName());
    CashCard savedCashCard = cashCardRepository.save(cashCardWithOwner);
    URI locationOfNewCashCard = ucb
        .path(Constants.URI.CASH_CARD + "/{id}")
        .buildAndExpand(savedCashCard.getId())
        .toUri();
    return ResponseEntity.created(locationOfNewCashCard).build();
  }

  @GetMapping
  public ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal) {
    Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(),
        PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
        ));
    return ResponseEntity.ok(page.getContent());
  }

  @PutMapping("/{requestedId}")
  private ResponseEntity<Void> putCashCard(@PathVariable Long requestedId, @RequestBody CashCard cashCardUpdate, Principal principal) {
    CashCard cashCard = findCashCard(requestedId, principal);
    if (cashCard != null) {
      CashCard updatedCashCard = new CashCard(requestedId, cashCardUpdate.getAmount(), principal.getName());
      cashCardRepository.save(updatedCashCard);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  private CashCard findCashCard(Long requestedId, Principal principal) {
    return cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
  }

  @DeleteMapping("/{requestedId}")
  private ResponseEntity<Void> deleteCashCard(@PathVariable Long requestedId, Principal principal){
    if (cashCardRepository.existsByIdAndOwner(requestedId, principal.getName())) {
      cashCardRepository.deleteById(requestedId);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}