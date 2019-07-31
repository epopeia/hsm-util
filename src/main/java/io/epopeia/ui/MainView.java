package io.epopeia.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.Route;

import io.epopeia.ui.layout.IssuersLayout;
import io.epopeia.ui.layout.NetworksLayout;

@Route
public class MainView extends AppLayout {
	private static final long serialVersionUID = 1L;

	@Autowired
	public MainView(IssuersLayout issuers, NetworksLayout networks) {
		super();
		setBranding(new H3("Epopeia Authorizator"));

		createMenu().addMenuItems(new AppLayoutMenuItem("Issuers", e -> setContent(issuers)),
				new AppLayoutMenuItem("Networks", e -> setContent(networks)));
	}
}
