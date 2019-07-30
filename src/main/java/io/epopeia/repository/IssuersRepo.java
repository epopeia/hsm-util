package io.epopeia.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import io.epopeia.domain.Issuers;

@RestResource(path = "issuers", rel = "issuers")
public interface IssuersRepo extends CrudRepository<Issuers, Long> {

}
