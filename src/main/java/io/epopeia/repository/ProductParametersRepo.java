package io.epopeia.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import io.epopeia.domain.ProductParameters;

@RestResource(path = "productParameters", rel = "productParameters")
public interface ProductParametersRepo extends CrudRepository<ProductParameters, Long> {

}
