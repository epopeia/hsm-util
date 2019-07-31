package io.epopeia.domain;

import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
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
public @Data class ProductParameters {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "parameter_id", referencedColumnName = "id")
	private Parameters parameters;

	@OneToOne
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private Products products;

	private String value;
	private Boolean active;

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
