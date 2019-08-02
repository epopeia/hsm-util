package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.epopeia.domain.Customers;

public interface CustomersRepo extends JpaRepository<Customers, Long> {

}
