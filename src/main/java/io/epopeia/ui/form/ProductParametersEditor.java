package io.epopeia.ui.form;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.epopeia.domain.Parameters;
import io.epopeia.domain.ProductParameters;
import io.epopeia.domain.Products;
import io.epopeia.repository.ParametersRepo;
import io.epopeia.repository.ProductParametersRepo;
import io.epopeia.repository.ProductsRepo;

@SpringComponent
@UIScope
public class ProductParametersEditor extends FormLayout implements KeyNotifier {
	private static final long serialVersionUID = 1L;

	ProductParametersRepo repository;
	ProductParameters entity;

	TextField value = new TextField("Value");
	Checkbox active = new Checkbox("Active", true);

	ComboBox<Parameters> parameters = new ComboBox<>("Parameters");
	ComboBox<Products> products = new ComboBox<>("Products");

	Button save = new Button("Save", VaadinIcon.CHECK.create());
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcon.TRASH.create());
	HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

	Binder<ProductParameters> binder = new Binder<>(ProductParameters.class);
	private ChangeHandler changeHandler;

	@Autowired
	public ProductParametersEditor(ProductParametersRepo repository, ParametersRepo parametersRepo, ProductsRepo productsRepo) {
		this.repository = repository;

		parameters.setItemLabelGenerator(Parameters::getName);
		parameters.addValueChangeListener(e -> entity.setParameters(parameters.getValue()));
		parameters.addFocusListener(e -> parameters.setItems(parametersRepo.findAll()));

		products.setItemLabelGenerator(Products::getName);
		products.addValueChangeListener(e -> entity.setProducts(products.getValue()));
		products.addFocusListener(e -> products.setItems(productsRepo.findAll()));

		add(value, parameters, products, active, actions);

		// bind using naming convention
		binder.bindInstanceFields(this);

		save.getElement().getThemeList().add("primary");
		delete.getElement().getThemeList().add("error");

		addKeyPressListener(Key.ENTER, e -> save());

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());
		cancel.addClickListener(e -> edit(entity));
		setVisible(false);
	}

	void delete() {
		repository.delete(entity);
		changeHandler.onChange();
	}

	void save() {
		repository.save(entity);
		changeHandler.onChange();
	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void edit(ProductParameters e) {
		parameters.clear();
		products.clear();
		if (e == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = e.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			entity = repository.findById(e.getId()).get();
		} else {
			entity = e;
		}
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		binder.setBean(entity);

		setVisible(true);

		// Focus initially
		value.focus();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete is clicked
		changeHandler = h;
	}
}
