package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.epopeia.domain.Cards;

public interface CardsRepo extends JpaRepository<Cards, Long> {

}
