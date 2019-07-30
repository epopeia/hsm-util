package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import io.epopeia.domain.CardProfiles;

@RestResource(path = "cardProfiles", rel = "cardProfiles")
public interface CardProfilesRepo extends JpaRepository<CardProfiles, Long> {

}
