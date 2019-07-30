package io.epopeia.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;

@Entity
public @Data class Products {

	@Id
	private Long id;

	private String name;
	private Long active;

	@OneToOne
	@JoinColumn(name = "network_id", referencedColumnName = "id")
	private Networks networks;

	@Exclude
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at = new Date();

	@Exclude
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_at = new Date();
}
