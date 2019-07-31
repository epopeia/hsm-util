package io.epopeia.domain;

import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;

@Entity
public @Data class Parameters {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String description;
	private String default_value;
	private Long active;

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
