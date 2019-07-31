package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.epopeia.domain.ProductParameters;

@RepositoryRestResource(path = "productParameters", collectionResourceRel = "productParameters")
public interface ProductParametersRepo extends JpaRepository<ProductParameters, Long> {

}
