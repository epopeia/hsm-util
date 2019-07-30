package io.epopeia.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import io.epopeia.domain.CardProfileParameters;

@RestResource(path = "cardProfileParameters", rel = "cardProfileParameters")
public interface CardProfileParametersRepo extends CrudRepository<CardProfileParameters, Long> {

}
