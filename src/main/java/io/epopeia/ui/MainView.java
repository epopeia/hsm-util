package io.epopeia.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.Route;

import io.epopeia.ui.layout.CustomersLayout;
import io.epopeia.ui.layout.IssuerProductsLayout;
import io.epopeia.ui.layout.IssuersLayout;
import io.epopeia.ui.layout.NetworksLayout;
import io.epopeia.ui.layout.ParametersLayout;
import io.epopeia.ui.layout.ProductsLayout;

@Route
public class MainView extends AppLayout {
	private static final long serialVersionUID = 1L;

	@Autowired
	public MainView(IssuersLayout issuers, NetworksLayout networks, ParametersLayout parameters,
			CustomersLayout customers, ProductsLayout products, IssuerProductsLayout issuerProducts) {
		super();
		setBranding(new H3("Epopeia Authorizator"));

		AppLayoutMenuItem mProducts = new AppLayoutMenuItem("Products", e -> setContent(products));
		AppLayoutMenuItem mCustomers = new AppLayoutMenuItem("Customers", e -> setContent(customers));
		AppLayoutMenuItem mParameters = new AppLayoutMenuItem("Parameters", e -> setContent(parameters));
		AppLayoutMenuItem mIssuers = new AppLayoutMenuItem("Issuers", e -> setContent(issuers));
		AppLayoutMenuItem mNetworks = new AppLayoutMenuItem("Networks", e -> setContent(networks));
		AppLayoutMenuItem mIssuerProducts = new AppLayoutMenuItem("Issuer Products", e -> setContent(issuerProducts));

		mProducts.addMenuItemClickListener(e -> products.refresh());
		mCustomers.addMenuItemClickListener(e -> customers.refresh());
		mParameters.addMenuItemClickListener(e -> parameters.refresh());
		mIssuers.addMenuItemClickListener(e -> issuers.refresh());
		mNetworks.addMenuItemClickListener(e -> networks.refresh());
		mIssuerProducts.addMenuItemClickListener(e -> issuerProducts.refresh());

		createMenu().addMenuItems(mIssuers, mIssuerProducts, mProducts, mNetworks, mCustomers, mParameters);
	}
}
