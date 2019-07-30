package io.epopeia.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import io.epopeia.domain.Customers;

@RestResource(path = "customers", rel = "customers")
public interface CustomersRepo extends CrudRepository<Customers, Long> {

}
