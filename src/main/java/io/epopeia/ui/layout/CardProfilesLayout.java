package io.epopeia.ui.layout;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.epopeia.domain.CardProfiles;
import io.epopeia.repository.CardProfilesRepo;
import io.epopeia.ui.form.CardProfilesEditor;

@SpringComponent
@UIScope
public class CardProfilesLayout extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	Grid<CardProfiles> grid = new Grid<>(CardProfiles.class, false);
	CardProfilesRepo repo;

	@Autowired
	public CardProfilesLayout(CardProfilesRepo repo, CardProfilesEditor form) {
		this.repo = repo;

		grid.setHeight("300px");
		grid.addColumn(p -> {
			if (p.getIssuerProducts() != null)
				return p.getIssuerProducts().getName();
			else
				return null;
		}).setHeader("Issuer Products");
		grid.addColumns("name", "description", "created_at", "updated_at");

		// Connect selected Entity to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			form.edit(e.getValue());
		});

		// Listen changes made by the editor, refresh data from back-end
		form.setChangeHandler(() -> {
			form.setVisible(false);
			refresh();
		});

		Button addBt = new Button(VaadinIcon.PLUS.create(), e -> form.edit(new CardProfiles()));
		addBt.setText("New " + CardProfiles.class.getSimpleName());
		HorizontalLayout actions = new HorizontalLayout(addBt);
		add(actions, grid, form);

		// Initialize listing
		refresh();
	}

	public void refresh() {
		grid.setItems(repo.findAll());
	}
}
