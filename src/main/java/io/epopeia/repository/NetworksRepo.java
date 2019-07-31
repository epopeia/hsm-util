package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.epopeia.domain.Networks;

@RepositoryRestResource(path = "networks", collectionResourceRel = "networks")
public interface NetworksRepo extends JpaRepository<Networks, Long> {

}
