package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.epopeia.domain.Products;

@RepositoryRestResource(path = "products", collectionResourceRel = "products")
public interface ProductsRepo extends JpaRepository<Products, Long> {

}
