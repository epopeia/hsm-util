package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.epopeia.domain.Issuers;

public interface IssuersRepo extends JpaRepository<Issuers, Long> {

}
