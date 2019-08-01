package io.epopeia.ui.layout;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.epopeia.domain.CardProfileParameters;
import io.epopeia.repository.CardProfileParametersRepo;
import io.epopeia.ui.form.CardProfileParametersEditor;

@SpringComponent
@UIScope
public class CardProfileParametersLayout extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	Grid<CardProfileParameters> grid = new Grid<>(CardProfileParameters.class, false);
	CardProfileParametersRepo repo;

	@Autowired
	public CardProfileParametersLayout(CardProfileParametersRepo repo, CardProfileParametersEditor form) {
		this.repo = repo;

		grid.setHeight("300px");
		grid.addColumn(p -> {
			if (p.getParameters() != null)
				return p.getParameters().getName();
			else
				return null;
		}).setHeader("Parameters");
		grid.addColumn(p -> {
			if (p.getCardProfiles() != null)
				return p.getCardProfiles().getName();
			else
				return null;
		}).setHeader("Card Profiles");
		grid.addColumns("value", "active", "created_at", "updated_at");

		// Connect selected Entity to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			form.edit(e.getValue());
		});

		// Listen changes made by the editor, refresh data from back-end
		form.setChangeHandler(() -> {
			form.setVisible(false);
			refresh();
		});

		Button addBt = new Button(VaadinIcon.PLUS.create(), e -> form.edit(new CardProfileParameters()));
		addBt.setText("New " + CardProfileParameters.class.getSimpleName());
		HorizontalLayout actions = new HorizontalLayout(addBt);
		add(actions, grid, form);

		// Initialize listing
		refresh();
	}

	public void refresh() {
		grid.setItems(repo.findAll());
	}
}
