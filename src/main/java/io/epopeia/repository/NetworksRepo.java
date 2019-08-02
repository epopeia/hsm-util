package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.epopeia.domain.Networks;

public interface NetworksRepo extends JpaRepository<Networks, Long> {

}
