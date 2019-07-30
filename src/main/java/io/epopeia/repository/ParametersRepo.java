package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import io.epopeia.domain.Parameters;

@RestResource(path = "parameters", rel = "parameters")
public interface ParametersRepo extends JpaRepository<Parameters, Long> {

}
