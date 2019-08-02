package io.epopeia.ui.layout;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.epopeia.domain.Products;
import io.epopeia.repository.ProductsRepo;
import io.epopeia.ui.form.ProductsEditor;

@SpringComponent
@UIScope
public class ProductsLayout extends VerticalLayout {
	private static final long serialVersionUID = 1L;
package io.epopeia.ui.layout;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.epopeia.domain.Products;
import io.epopeia.repository.ProductsRepo;
import io.epopeia.ui.form.ProductsEditor;

@SpringComponent
@UIScope
public class ProductsLayout extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	Grid<Products> grid = new Grid<>(Products.class, false);
	ProductsRepo repo;

	@Autowired
	public ProductsLayout(ProductsRepo repo, ProductsEditor form) {
		this.repo = repo;

		grid.setHeight("300px");
		grid.addColumn(p -> {
			if (p.getNetworks() != null)
				return p.getNetworks().getName();
			else
				return null;
		}).setHeader("Networks");
		grid.addColumns("name", "active", "created_at", "updated_at");

		// Connect selected Entity to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			form.edit(e.getValue());
		});

		// Listen changes made by the editor, refresh data from back-end
		form.setChangeHandler(() -> {
			form.setVisible(false);
			refresh();
		});

		Button addBt = new Button(VaadinIcon.PLUS.create(), e -> form.edit(new Products()));
		addBt.setText("New " + Products.class.getSimpleName());
		HorizontalLayout actions = new HorizontalLayout(addBt);
		add(actions, grid, form);

		// Initialize listing
		refresh();
	}

	public void refresh() {
		grid.setItems(repo.findAll());
	}
}

	Grid<Products> grid = new Grid<>(Products.class, false);
	ProductsRepo repo;

	@Autowired
	public ProductsLayout(ProductsRepo repo, ProductsEditor form) {
		this.repo = repo;

		grid.setHeight("300px");
		grid.addColumn(p -> {
			if (p.getNetworks() != null)
				return p.getNetworks().getName();
			else
				return null;
		}).setHeader("Networks");
		grid.addColumns("name", "active", "created_at", "updated_at");

		// Connect selected Entity to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			form.edit(e.getValue());
		});

		// Listen changes made by the editor, refresh data from back-end
		form.setChangeHandler(() -> {
			form.setVisible(false);
			refresh();
		});

		Button addBt = new Button(VaadinIcon.PLUS.create(), e -> form.edit(new Products()));
		addBt.setText("New " + Products.class.getSimpleName());
		HorizontalLayout actions = new HorizontalLayout(addBt);
		add(actions, grid, form);

		// Initialize listing
		refresh();
	}

	public void refresh() {
		grid.setItems(repo.findAll());
	}
}
