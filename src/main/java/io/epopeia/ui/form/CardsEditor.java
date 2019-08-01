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

import io.epopeia.domain.CardProfiles;
import io.epopeia.domain.Cards;
import io.epopeia.domain.Customers;
import io.epopeia.domain.IssuerProducts;
import io.epopeia.repository.CardProfilesRepo;
import io.epopeia.repository.CardsRepo;
import io.epopeia.repository.CustomersRepo;
import io.epopeia.repository.IssuerProductsRepo;

@SpringComponent
@UIScope
public class CardsEditor extends FormLayout implements KeyNotifier {
	private static final long serialVersionUID = 1L;

	CardsRepo repository;
	Cards entity;

	TextField masked_card = new TextField("Masked Card");
	TextField card = new TextField("Card");
	TextField hash = new TextField("Hash");
	NumberField expiration_month = new NumberField("Expiration Month");
	NumberField expiration_year = new NumberField("Expiration Year");
	Checkbox active = new Checkbox("Active", true);

	ComboBox<Cards> parentCard = new ComboBox<>("Parent Card");
	ComboBox<Customers> customers = new ComboBox<>("Customers");
	ComboBox<IssuerProducts> issuerProducts = new ComboBox<>("Issuer Products");
	ComboBox<CardProfiles> cardProfiles = new ComboBox<>("Card Profiles");

	Button save = new Button("Save", VaadinIcon.CHECK.create());
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcon.TRASH.create());
	HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

	Binder<Cards> binder = new Binder<>(Cards.class);
	private ChangeHandler changeHandler;

	@Autowired
	public CardsEditor(CardsRepo repository, IssuerProductsRepo issuerProductsRepo, CardProfilesRepo cardProfilesRepo,
			CustomersRepo customersRepo, CardsRepo cardsRepo) {
		this.repository = repository;

		issuerProducts.setItemLabelGenerator(IssuerProducts::getName);
		issuerProducts.addValueChangeListener(e -> entity.setIssuerProducts(issuerProducts.getValue()));
		issuerProducts.addFocusListener(e -> issuerProducts.setItems(issuerProductsRepo.findAll()));

		parentCard.setItemLabelGenerator(Cards::getCard);
		parentCard.addValueChangeListener(e -> entity.setParentCard(parentCard.getValue()));
		parentCard.addFocusListener(e -> parentCard.setItems(cardsRepo.findAll()));

		customers.setItemLabelGenerator(Customers::getDocument);
		customers.addValueChangeListener(e -> entity.setCustomers(customers.getValue()));
		customers.addFocusListener(e -> customers.setItems(customersRepo.findAll()));

		cardProfiles.setItemLabelGenerator(CardProfiles::getName);
		cardProfiles.addValueChangeListener(e -> entity.setCardProfiles(cardProfiles.getValue()));
		cardProfiles.addFocusListener(e -> cardProfiles.setItems(cardProfilesRepo.findAll()));

		add(masked_card, card, hash, expiration_month, expiration_year, issuerProducts, customers, parentCard, cardProfiles, active, actions);

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

	public final void edit(Cards e) {
		customers.clear();
		parentCard.clear();
		issuerProducts.clear();
		cardProfiles.clear();
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
		card.focus();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete is clicked
		changeHandler = h;
	}
}
