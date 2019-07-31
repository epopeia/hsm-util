package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.epopeia.domain.CardProfileParameters;

@RepositoryRestResource(path = "cardProfileParameters", collectionResourceRel = "cardProfileParameters")
public interface CardProfileParametersRepo extends JpaRepository<CardProfileParameters, Long> {

}
