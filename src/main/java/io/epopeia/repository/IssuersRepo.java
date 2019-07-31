package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.epopeia.domain.Issuers;

@RepositoryRestResource(path = "issuers", collectionResourceRel = "issuers")
public interface IssuersRepo extends JpaRepository<Issuers, Long> {

}
