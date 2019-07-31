package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.epopeia.domain.CardProfiles;

@RepositoryRestResource(path = "cardProfiles", collectionResourceRel = "cardProfiles")
public interface CardProfilesRepo extends JpaRepository<CardProfiles, Long> {

}
