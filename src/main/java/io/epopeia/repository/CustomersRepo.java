package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.epopeia.domain.Customers;

@RepositoryRestResource(path = "customers", collectionResourceRel = "customers")
public interface CustomersRepo extends JpaRepository<Customers, Long> {

}
