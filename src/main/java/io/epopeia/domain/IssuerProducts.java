package io.epopeia.domain;

import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;

@Entity
public @Data class IssuerProducts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "issuer_id", referencedColumnName = "id")
	private Issuers issuers;

	@OneToOne
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private Products products;

	private String bin;
	private Long range_start;
	private Long range_end;
	private Long card_length;
	private String name;
	private String network_ica_id;
	private Long active;

	@Exclude
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at = new Date();

	@Exclude
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_at = new Date();
}
