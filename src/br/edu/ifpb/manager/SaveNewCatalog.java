package br.edu.ifpb.manager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.sis.xml.MarshallerPool;
import org.geotoolkit.csw.CatalogServicesServer;
import org.geotoolkit.csw.GetCapabilitiesRequest;
import org.geotoolkit.csw.xml.CSWMarshallerPool;
import org.geotoolkit.csw.xml.v202.Capabilities;

import br.edu.ifpb.daos.CatalogServiceDAO;
import br.edu.ifpb.entidades.Catalogservice;

public class SaveNewCatalog {
	
	private final MarshallerPool pool;
	private final Unmarshaller um;
	private CatalogServiceDAO catalogServiceDAO;
	private Catalogservice catalogService;
	
	public SaveNewCatalog() throws JAXBException{
		
		pool = CSWMarshallerPool.getInstance();
		um = pool.acquireUnmarshaller();
		catalogService = new Catalogservice();
		catalogServiceDAO = new CatalogServiceDAO();
	}
	
	public boolean addCatalog(String url) throws IOException, JAXBException{
		
		final CatalogServicesServer cswServer = new CatalogServicesServer(new URL(url),	"2.0.2");
		final GetCapabilitiesRequest getCapa = cswServer.createGetCapabilities();
		
		InputStream is = getCapa.getResponseStream();
		Capabilities capabilities = (Capabilities) um.unmarshal(is);

		catalogService.setProvider(capabilities.getServiceProvider().getProviderName());
		catalogService.setTextdescription(capabilities.getServiceIdentification().getAbstract());
		catalogService.setUrl(capabilities.getServiceProvider().getProviderSite().getHref());
		
		catalogServiceDAO.save(catalogService);
		return true;
	}
}

	
