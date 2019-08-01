package io.epopeia.ui.layout;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.epopeia.domain.Parameters;
import io.epopeia.repository.ParametersRepo;
import io.epopeia.ui.form.ParametersEditor;

@SpringComponent
@UIScope
public class ParametersLayout extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	Grid<Parameters> grid = new Grid<>(Parameters.class, false);
	ParametersRepo repo;

	@Autowired
	public ParametersLayout(ParametersRepo repo, ParametersEditor form) {
		this.repo = repo;

		grid.setHeight("300px");
		grid.setColumns("name", "description", "default_value", "active", "created_at", "updated_at");

		// Connect selected Entity to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			form.edit(e.getValue());
		});

		// Listen changes made by the editor, refresh data from back-end
		form.setChangeHandler(() -> {
			form.setVisible(false);
			refresh();
		});

		Button addBt = new Button(VaadinIcon.PLUS.create(), e -> form.edit(new Parameters()));
		addBt.setText("New " + Parameters.class.getSimpleName());
		HorizontalLayout actions = new HorizontalLayout(addBt);
		add(actions, grid, form);

		// Initialize listing
		refresh();
	}

	public void refresh() {
		grid.setItems(repo.findAll());
	}
}
