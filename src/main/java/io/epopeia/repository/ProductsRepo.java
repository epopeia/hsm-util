package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import io.epopeia.domain.Products;

@RestResource(path = "products", rel = "products")
public interface ProductsRepo extends JpaRepository<Products, Long> {

}
