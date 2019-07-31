package io.epopeia.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;

@Entity
public @Data class CardProfileParameters {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String value;
	private Long active;

	@OneToOne
	@JoinColumn(name = "card_profiles_id", referencedColumnName = "id")
	private CardProfiles cardProfiles;

	@OneToOne
	@JoinColumn(name = "parameter_id", referencedColumnName = "id")
	private Parameters parameters;

	@Exclude
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;

	@Exclude
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_at;

	@PrePersist
	public void prePersist() {
		created_at = new Date();
	}

	@PreUpdate
	public void preUpdate() {
		updated_at = new Date();
	}
}
