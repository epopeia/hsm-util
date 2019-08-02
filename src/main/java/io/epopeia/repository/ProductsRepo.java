package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.epopeia.domain.Products;

public interface ProductsRepo extends JpaRepository<Products, Long> {

}
