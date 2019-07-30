package io.epopeia.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import io.epopeia.domain.Parameters;

@RestResource(path = "parameters", rel = "parameters")
public interface ParametersRepo extends CrudRepository<Parameters, Long> {

}
