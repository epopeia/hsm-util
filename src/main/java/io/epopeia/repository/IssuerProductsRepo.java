package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import io.epopeia.domain.IssuerProducts;

@RestResource(path = "issuerProducts", rel = "issuerProducts")
public interface IssuerProductsRepo extends JpaRepository<IssuerProducts, Long> {

}
