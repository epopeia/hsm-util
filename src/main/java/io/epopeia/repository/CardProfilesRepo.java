package io.epopeia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.epopeia.domain.CardProfiles;

public interface CardProfilesRepo extends JpaRepository<CardProfiles, Long> {

}
