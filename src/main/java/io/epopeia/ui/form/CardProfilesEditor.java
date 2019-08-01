package io.epopeia.ui.form;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import io.epopeia.domain.CardProfiles;
import io.epopeia.domain.IssuerProducts;
import io.epopeia.repository.CardProfilesRepo;
import io.epopeia.repository.IssuerProductsRepo;

@SpringComponent
@UIScope
public class CardProfilesEditor extends FormLayout implements KeyNotifier {
	private static final long serialVersionUID = 1L;

	CardProfilesRepo repository;
	CardProfiles entity;

	TextField name = new TextField("Name");
	TextField description = new TextField("Description");
	ComboBox<IssuerProducts> issuerProducts = new ComboBox<>("Issuer Products");

	Button save = new Button("Save", VaadinIcon.CHECK.create());
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcon.TRASH.create());
	HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

	Binder<CardProfiles> binder = new Binder<>(CardProfiles.class);
	private ChangeHandler changeHandler;

	@Autowired
	public CardProfilesEditor(CardProfilesRepo repository, IssuerProductsRepo issuerProductsRepo) {
		this.repository = repository;

		issuerProducts.setItemLabelGenerator(IssuerProducts::getName);
		issuerProducts.addValueChangeListener(e -> entity.setIssuerProducts(issuerProducts.getValue()));
		issuerProducts.addFocusListener(e -> issuerProducts.setItems(issuerProductsRepo.findAll()));

		add(name, description, issuerProducts, actions);

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

	public final void edit(CardProfiles e) {
		issuerProducts.clear();
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
