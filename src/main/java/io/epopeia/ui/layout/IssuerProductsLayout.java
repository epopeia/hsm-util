package io.epopeia.ui.layout;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.epopeia.domain.IssuerProducts;
import io.epopeia.repository.IssuerProductsRepo;
import io.epopeia.ui.form.IssuerProductsEditor;

@SpringComponent
@UIScope
public class IssuerProductsLayout extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	Grid<IssuerProducts> grid = new Grid<>(IssuerProducts.class, false);
	IssuerProductsRepo repo;

	@Autowired
	public IssuerProductsLayout(IssuerProductsRepo repo, IssuerProductsEditor form) {
		this.repo = repo;

		grid.setHeight("300px");
		grid.addColumn(p -> {
			if (p.getIssuers() != null)
				return p.getIssuers().getName();
			else
				return null;
		}).setHeader("Issuers");
		grid.addColumn(p -> {
			if (p.getProducts() != null)
				return p.getProducts().getName();
			else
				return null;
		}).setHeader("Products");
		grid.addColumns("name", "bin", "range_start", "range_end", "card_length", "network_ica_id", "active", "created_at", "updated_at");

		// Connect selected Entity to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			form.edit(e.getValue());
		});

		// Listen changes made by the editor, refresh data from back-end
		form.setChangeHandler(() -> {
			form.setVisible(false);
			refresh();
		});

		Button addBt = new Button(VaadinIcon.PLUS.create(), e -> form.edit(new IssuerProducts()));
		addBt.setText("New " + IssuerProducts.class.getSimpleName());
		HorizontalLayout actions = new HorizontalLayout(addBt);
		add(actions, grid, form);

		// Initialize listing
		refresh();
	}

	public void refresh() {
		grid.setItems(repo.findAll());
	}
}
