package br.edu.ifpb.servicos;

import java.sql.Timestamp;

import org.geotoolkit.csw.CatalogServicesServer;
import org.geotoolkit.csw.xml.v202.Capabilities;

import br.edu.ifpb.daos.CatalogServiceDAO;
import br.edu.ifpb.entidades.Catalogservice;

public class InformationOfCatalog {
	private Catalogservice catalog;
	private CatalogServiceDAO catalogDAO;

	public InformationOfCatalog() {
		this.catalog = new Catalogservice();
		this.catalogDAO = new CatalogServiceDAO();
	}

	public Catalogservice getCatalog() {
		return this.catalog;
	}

	private void setCatalog(Capabilities capabilities,
			CatalogServicesServer cswServer) {
		this.catalog.setLastcrawling(new Timestamp(System.currentTimeMillis()));
		this.catalog.setProvider(capabilities.getServiceProvider()
				.getProviderName());
		this.catalog.setTextdescription(capabilities.getServiceIdentification()
				.getAbstract());
		this.catalog.setUrl(cswServer.getURL().toString());
	}

	public void persistsInformationFromCatalog(Capabilities capabilities,
			CatalogServicesServer cswServer) {
		this.setCatalog(capabilities, cswServer);
		this.catalogDAO.save(this.getCatalog());
	}

	@Override
	public String toString() {
		return "InformationOfCatalog - Provide of Catalog:"
				+ catalog.getProvider();
	}

}
