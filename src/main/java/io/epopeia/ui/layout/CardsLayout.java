package io.epopeia.ui.layout;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.epopeia.domain.Cards;
import io.epopeia.repository.CardsRepo;
import io.epopeia.ui.form.CardsEditor;

@SpringComponent
@UIScope
public class CardsLayout extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	Grid<Cards> grid = new Grid<>(Cards.class, false);
	CardsRepo repo;

	@Autowired
	public CardsLayout(CardsRepo repo, CardsEditor form) {
		this.repo = repo;

		grid.setHeight("300px");
		grid.addColumn(p -> {
			if (p.getCustomers() != null)
				return p.getCustomers().getDocument();
			else
				return null;
		}).setHeader("Customers");
		grid.addColumn(p -> {
			if (p.getIssuerProducts() != null)
				return p.getIssuerProducts().getName();
			else
				return null;
		}).setHeader("IssuerProducts");
		grid.addColumn(p -> {
			if (p.getParentCard() != null)
				return p.getParentCard().getCard();
			else
				return null;
		}).setHeader("Parent Card");
		grid.addColumn(p -> {
			if (p.getCardProfiles() != null)
				return p.getCardProfiles().getName();
			else
				return null;
		}).setHeader("Card Profiles");
		grid.addColumns("masked_card", "card", "hash", "expiration_month", "expiration_year", "parentCard", "customers", "issuerProducts", "cardProfiles", "active", "created_at");

		// Connect selected Entity to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			form.edit(e.getValue());
		});

		// Listen changes made by the editor, refresh data from back-end
		form.setChangeHandler(() -> {
			form.setVisible(false);
			refresh();
		});

		Button addBt = new Button(VaadinIcon.PLUS.create(), e -> form.edit(new Cards()));
		addBt.setText("New " + Cards.class.getSimpleName());
		HorizontalLayout actions = new HorizontalLayout(addBt);
		add(actions, grid, form);

		// Initialize listing
		refresh();
	}

	public void refresh() {
		grid.setItems(repo.findAll());
	}
}
