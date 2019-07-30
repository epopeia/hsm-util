package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import io.epopeia.domain.ProductParameters;

@RestResource(path = "productParameters", rel = "productParameters")
public interface ProductParametersRepo extends JpaRepository<ProductParameters, Long> {

}
