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
public @Data class Cards {

	@Id
	private Long id;

	private String masked_card;
	private String card;
	private Long expiration_month;
	private Long expiration_year;
	private Long active;
	private String hash;

	@OneToOne
	@JoinColumn(name = "issuer_products_id", referencedColumnName = "id")
	private IssuerProducts issuerProducts;

	@OneToOne
	@JoinColumn(name = "card_profiles_id", referencedColumnName = "id")
	private CardProfiles cardProfiles;

	@OneToOne
	@JoinColumn(name = "card_id_parent", referencedColumnName = "id")
	private Cards parentCard;

	@OneToOne
	@JoinColumn(name = "customer_id", referencedColumnName = "id")
	private Customers customers;

	@Exclude
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at = new Date();
}