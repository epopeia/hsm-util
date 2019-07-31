package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.epopeia.domain.Cards;

@RepositoryRestResource(path = "cards", collectionResourceRel = "cards")
public interface CardsRepo extends JpaRepository<Cards, Long> {

}
