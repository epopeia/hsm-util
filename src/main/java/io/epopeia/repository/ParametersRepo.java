package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.epopeia.domain.Parameters;

@RepositoryRestResource(path = "parameters", collectionResourceRel = "parameters")
public interface ParametersRepo extends JpaRepository<Parameters, Long> {

}
