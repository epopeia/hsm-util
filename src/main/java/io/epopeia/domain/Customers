package io.epopeia.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;

@Entity
public @Data class Customers {

	@Id
	private Long id;

	private String document;
	private String customer_data_json;
	private Long active;

	@Exclude
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at = new Date();

	@Exclude
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_at = new Date();
}
