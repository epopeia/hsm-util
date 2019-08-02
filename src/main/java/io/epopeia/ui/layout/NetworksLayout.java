package io.epopeia.ui.layout;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.epopeia.domain.Networks;
import io.epopeia.repository.NetworksRepo;
import io.epopeia.ui.form.NetworksEditor;

@SpringComponent
@UIScope
public class NetworksLayout extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	Grid<Networks> grid = new Grid<>(Networks.class, false);
	NetworksRepo repo;

	@Autowired
	public NetworksLayout(NetworksRepo repo, NetworksEditor form) {
		this.repo = repo;

		grid.setHeight("300px");
		grid.setColumns("name", "active", "created_at", "updated_at");

		// Connect selected Entity to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			form.edit(e.getValue());
		});

		// Listen changes made by the editor, refresh data from back-end
		form.setChangeHandler(() -> {
			form.setVisible(false);
			refresh();
		});

		Button addBt = new Button(VaadinIcon.PLUS.create(), e -> form.edit(new Networks()));
		addBt.setText("New " + Networks.class.getSimpleName());
		HorizontalLayout actions = new HorizontalLayout(addBt);
		add(actions, grid, form);

		// Initialize listing
		refresh();
	}

	public void refresh() {
		grid.setItems(repo.findAll());
	}
}
