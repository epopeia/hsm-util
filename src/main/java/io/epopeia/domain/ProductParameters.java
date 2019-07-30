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
public @Data class ProductParameters {

	@Id
	private Long id;

	@OneToOne
	@JoinColumn(name = "parameter_id", referencedColumnName = "id")
	private Parameters parameters;

	@OneToOne
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private Products products;

	private String value;
	private Long active;

	@Exclude
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at = new Date();

	@Exclude
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_at = new Date();
}
