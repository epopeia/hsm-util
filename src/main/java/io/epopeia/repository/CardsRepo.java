package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import io.epopeia.domain.Cards;

@RestResource(path = "cards", rel = "cards")
public interface CardsRepo extends JpaRepository<Cards, Long> {

}
