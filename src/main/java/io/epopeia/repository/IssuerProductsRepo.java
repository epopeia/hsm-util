package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.epopeia.domain.IssuerProducts;

public interface IssuerProductsRepo extends JpaRepository<IssuerProducts, Long> {

}
