package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import io.epopeia.domain.Networks;

@RestResource(path = "networks", rel = "networks")
public interface NetworksRepo extends JpaRepository<Networks, Long> {

}
