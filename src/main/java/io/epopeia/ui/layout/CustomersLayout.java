package io.epopeia.ui.layout;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.epopeia.domain.Customers;
import io.epopeia.repository.CustomersRepo;
import io.epopeia.ui.form.CustomersEditor;

@SpringComponent
@UIScope
public class CustomersLayout extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	Grid<Customers> grid = new Grid<>(Customers.class, false);
	CustomersRepo repo;

	@Autowired
	public CustomersLayout(CustomersRepo repo, CustomersEditor form) {
		this.repo = repo;

		grid.setHeight("300px");
		grid.setColumns("document", "customer_data_json", "active", "created_at", "updated_at");

		// Connect selected Entity to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			form.edit(e.getValue());
		});

		// Listen changes made by the editor, refresh data from back-end
		form.setChangeHandler(() -> {
			form.setVisible(false);
			refresh();
		});

		Button addBt = new Button(VaadinIcon.PLUS.create(), e -> form.edit(new Customers()));
		addBt.setText("New " + Customers.class.getSimpleName());
		HorizontalLayout actions = new HorizontalLayout(addBt);
		add(actions, grid, form);

		// Initialize listing
		refresh();
	}

	public void refresh() {
		grid.setItems(repo.findAll());
	}
}
