package io.epopeia.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import io.epopeia.domain.Customers;
import io.epopeia.domain.Issuers;
import io.epopeia.domain.Networks;
import io.epopeia.domain.Parameters;
import io.epopeia.repository.CustomersRepo;
import io.epopeia.repository.IssuersRepo;
import io.epopeia.repository.NetworksRepo;
import io.epopeia.repository.ParametersRepo;

@Route
public class MainView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	IssuersRepo issuersRepo; 
	IssuersEditor issuersEditor;
	Grid<Issuers> issuersGrid = new Grid<>(Issuers.class);
	Button issuersAddBt = new Button("New Issuer", VaadinIcon.PLUS.create());

	CustomersRepo customersRepo; 
	CustomersEditor customersEditor;
	Grid<Customers> customersGrid = new Grid<>(Customers.class);
	Button customersAddBt = new Button("New Customer", VaadinIcon.PLUS.create());

	NetworksRepo networksRepo;
	NetworksEditor networksEditor;
	Grid<Networks> networksGrid = new Grid<>(Networks.class);
	Button networksAddBt = new Button("New Network", VaadinIcon.PLUS.create());
	
	ParametersRepo parametersRepo; 
	ParametersEditor parametersEditor;
	Grid<Parameters> parametersGrid = new Grid<>(Parameters.class);
	Button parametersAddBt = new Button("New Parameter", VaadinIcon.PLUS.create());

	@Autowired
	public MainView(
			IssuersRepo issuersRepo, IssuersEditor issuersEditor,
			CustomersRepo customersRepo, CustomersEditor customersEditor,
			NetworksRepo networksRepo, NetworksEditor networksEditor,
			ParametersRepo parametersRepo, ParametersEditor parametersEditor
			) {

		this.customersRepo = customersRepo;
		this.customersEditor = customersEditor;
		HorizontalLayout customerActions = new HorizontalLayout(customersAddBt);

		customersGrid.setHeight("100px");
		customersGrid.setColumns("id", "document", "customer_data_json");
		customersGrid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

		// Connect selected Customer to editor or hide if none is selected
		customersGrid.asSingleSelect().addValueChangeListener(e -> {
			this.customersEditor.edit(e.getValue());
		});

		// Instantiate and edit new Customer the new button is clicked
		customersAddBt.addClickListener(e -> customersEditor.edit(new Customers()));

		// Listen changes made by the editor, refresh data from backend
		customersEditor.setChangeHandler(() -> {
			customersEditor.setVisible(false);
			customersGrid.setItems(customersRepo.findAll());
		});

		// Initialize listing
		customersGrid.setItems(customersRepo.findAll());
		
		// ==================================================================
		// ==================================================================
		// ==================================================================
		
		this.networksRepo = networksRepo;
		this.networksEditor = networksEditor;
		HorizontalLayout networkActions = new HorizontalLayout(networksAddBt);

		networksGrid.setHeight("100px");
		networksGrid.setColumns("id", "name");
		networksGrid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

		// Connect selected Customer to editor or hide if none is selected
		networksGrid.asSingleSelect().addValueChangeListener(e -> {
			this.networksEditor.edit(e.getValue());
		});

		// Instantiate and edit new Customer the new button is clicked
		networksAddBt.addClickListener(e -> networksEditor.edit(new Networks()));

		// Listen changes made by the editor, refresh data from backend
		networksEditor.setChangeHandler(() -> {
			networksEditor.setVisible(false);
			networksGrid.setItems(networksRepo.findAll());
		});

		// ==================================================================
		// ==================================================================
		// ==================================================================
		
		this.issuersRepo = issuersRepo;
		this.issuersEditor = issuersEditor;
		HorizontalLayout issuerActions = new HorizontalLayout(issuersAddBt);

		issuersGrid.setHeight("100px");
		issuersGrid.setColumns("id", "name");
		issuersGrid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

		// Connect selected Customer to editor or hide if none is selected
		issuersGrid.asSingleSelect().addValueChangeListener(e -> {
			this.issuersEditor.edit(e.getValue());
		});

		// Instantiate and edit new Customer the new button is clicked
		issuersAddBt.addClickListener(e -> issuersEditor.edit(new Issuers()));

		// Listen changes made by the editor, refresh data from backend
		issuersEditor.setChangeHandler(() -> {
			issuersEditor.setVisible(false);
			issuersGrid.setItems(issuersRepo.findAll());
		});

		// ==================================================================
		// ==================================================================
		// ==================================================================
		
		this.parametersRepo = parametersRepo;
		this.parametersEditor = parametersEditor;
		HorizontalLayout parameterActions = new HorizontalLayout(parametersAddBt);

		parametersGrid.setHeight("100px");
		parametersGrid.setColumns("id", "name", "description", "default_value");
		parametersGrid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

		// Connect selected Customer to editor or hide if none is selected
		parametersGrid.asSingleSelect().addValueChangeListener(e -> {
			this.parametersEditor.edit(e.getValue());
		});

		// Instantiate and edit new Customer the new button is clicked
		parametersAddBt.addClickListener(e -> parametersEditor.edit(new Parameters()));

		// Listen changes made by the editor, refresh data from backend
		parametersEditor.setChangeHandler(() -> {
			parametersEditor.setVisible(false);
			parametersGrid.setItems(parametersRepo.findAll());
		});
		
		// Insert grids
		add(
				issuerActions, issuersGrid, issuersEditor,
				customerActions, customersGrid, customersEditor,
				networkActions, networksGrid, networksEditor,
				parameterActions, parametersGrid, parametersEditor
			);

		// Initialize listing
		issuersGrid.setItems(issuersRepo.findAll());
		customersGrid.setItems(customersRepo.findAll());
		networksGrid.setItems(networksRepo.findAll());
		parametersGrid.setItems(parametersRepo.findAll());
	}
}
