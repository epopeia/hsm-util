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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.epopeia.domain.IssuerProducts;
import io.epopeia.domain.Issuers;
import io.epopeia.domain.Products;
import io.epopeia.repository.IssuerProductsRepo;
import io.epopeia.repository.IssuersRepo;
import io.epopeia.repository.ProductsRepo;

@SpringComponent
@UIScope
public class IssuerProductsEditor extends FormLayout implements KeyNotifier {
	private static final long serialVersionUID = 1L;

	IssuerProductsRepo repository;
	IssuerProducts entity;

	TextField name = new TextField("Name");
	TextField bin = new TextField("Bin");
	NumberField range_start = new NumberField("Range Start");
	NumberField range_end = new NumberField("Range End");
	NumberField card_length = new NumberField("Card Length");
	TextField network_ica_id = new TextField("Network ICA id");
	Checkbox active = new Checkbox("Active", true);

	ComboBox<Issuers> issuers = new ComboBox<>("Issuers");
	ComboBox<Products> products = new ComboBox<>("Products");

	Button save = new Button("Save", VaadinIcon.CHECK.create());
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcon.TRASH.create());
	HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

	Binder<IssuerProducts> binder = new Binder<>(IssuerProducts.class);
	private ChangeHandler changeHandler;

	@Autowired
	public IssuerProductsEditor(IssuerProductsRepo repository, IssuersRepo issuersRepo, ProductsRepo productsRepo) {
		this.repository = repository;

		issuers.setItemLabelGenerator(Issuers::getName);
		issuers.addValueChangeListener(e -> entity.setIssuers(issuers.getValue()));
		issuers.addFocusListener(e -> issuers.setItems(issuersRepo.findAll()));

		products.setItemLabelGenerator(Products::getName);
		products.addValueChangeListener(e -> entity.setProducts(products.getValue()));
		products.addFocusListener(e -> products.setItems(productsRepo.findAll()));

		add(name, bin, range_start, range_end, card_length, network_ica_id, issuers, products, active, actions);

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

	public final void edit(IssuerProducts e) {
		issuers.clear();
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
		name.focus();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete is clicked
		changeHandler = h;
	}
}
