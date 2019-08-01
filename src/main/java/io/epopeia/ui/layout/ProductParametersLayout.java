package io.epopeia.ui.layout;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.epopeia.domain.ProductParameters;
import io.epopeia.repository.ProductParametersRepo;
import io.epopeia.ui.form.ProductParametersEditor;

@SpringComponent
@UIScope
public class ProductParametersLayout extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	Grid<ProductParameters> grid = new Grid<>(ProductParameters.class, false);
	ProductParametersRepo repo;

	@Autowired
	public ProductParametersLayout(ProductParametersRepo repo, ProductParametersEditor form) {
		this.repo = repo;

		grid.setHeight("300px");
		grid.addColumn(p -> {
			if (p.getParameters() != null)
				return p.getParameters().getName();
			else
				return null;
		}).setHeader("Parameters");
		grid.addColumn(p -> {
			if (p.getProducts() != null)
				return p.getProducts().getName();
			else
				return null;
		}).setHeader("Products");
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

		Button addBt = new Button(VaadinIcon.PLUS.create(), e -> form.edit(new ProductParameters()));
		addBt.setText("New " + ProductParameters.class.getSimpleName());
		HorizontalLayout actions = new HorizontalLayout(addBt);
		add(actions, grid, form);

		// Initialize listing
		refresh();
	}

	public void refresh() {
		grid.setItems(repo.findAll());
	}
}
