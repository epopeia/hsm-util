package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.epopeia.domain.IssuerProducts;

@RepositoryRestResource(path = "issuerProducts", collectionResourceRel = "issuerProducts")
public interface IssuerProductsRepo extends JpaRepository<IssuerProducts, Long> {

}
