package io.epopeia.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import io.epopeia.domain.Products;

@RestResource(path = "products", rel = "products")
public interface ProductsRepo extends CrudRepository<Products, Long> {

}
