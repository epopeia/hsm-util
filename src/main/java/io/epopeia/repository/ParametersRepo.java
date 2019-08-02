package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.epopeia.domain.Parameters;

public interface ParametersRepo extends JpaRepository<Parameters, Long> {

}
