package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.epopeia.domain.ProductParameters;

public interface ProductParametersRepo extends JpaRepository<ProductParameters, Long> {

}
