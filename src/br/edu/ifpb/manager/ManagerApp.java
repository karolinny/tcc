package br.edu.ifpb.manager;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.sis.metadata.iso.citation.DefaultOnlineResource;
import org.apache.sis.xml.MarshallerPool;
import org.geotoolkit.csw.CatalogServicesServer;
import org.geotoolkit.csw.GetCapabilitiesRequest;
import org.geotoolkit.csw.GetRecordByIdRequest;
import org.geotoolkit.csw.GetRecordsRequest;
import org.geotoolkit.csw.xml.CSWMarshallerPool;
import org.geotoolkit.csw.xml.GetRecordByIdResponse;
import org.geotoolkit.csw.xml.ResultType;
import org.geotoolkit.csw.xml.v202.Capabilities;
import org.geotoolkit.csw.xml.v202.GetRecordsResponseType;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.opengis.metadata.Metadata;

import br.edu.ifpb.daos.CatalogServiceDAO;
import br.edu.ifpb.daos.MetadataDAO;
import br.edu.ifpb.entidades.Catalogservice;
import br.edu.ifpb.entidades.Metadatarecord;
import br.edu.ifpb.entidades.Metadatarecordurl;
import br.edu.ifpb.test.Arquivo;

public class ManagerApp {

	public static void main(String[] args) throws MalformedURLException, URISyntaxException, IOException, JAXBException {

		final MarshallerPool pool = CSWMarshallerPool.getInstance();
		final Unmarshaller um = pool.acquireUnmarshaller();
		CatalogServiceDAO csd = new CatalogServiceDAO();
		Arquivo arquivo = new Arquivo();
		
		EntityManagerFactory emf;
		EntityManager em;
		
		
			emf = Persistence.createEntityManagerFactory("tcc-up");
			em = emf.createEntityManager();
			em.clear();
		

			String inde = "http://www.metadados.inde.gov.br/geonetwork/srv/br/csw?";
			String ana = "http://metadados.ana.gov.br/geonetwork/srv/pt/csw?";
			String ideandalucia = "http://www.ideandalucia.es/catalogodeservicios/srv/es/csw?";
			String idee = "http://www.idee.es/csw-inspire-idee/srv/eng/csw?";
			String geoportaligm = "http://www.geoportaligm.gob.ec/geonetwork/srv/es/csw?";
			String aesa ="http://geo.aesa.pb.gov.br/csw?";
			
		Catalogservice catalogService = new Catalogservice();
		final CatalogServicesServer cswServer = new CatalogServicesServer(new URL(geoportaligm),	"2.0.2");

		
		final GetCapabilitiesRequest getCapa = cswServer.createGetCapabilities();
		System.out.println(getCapa.getURL());
		InputStream is = getCapa.getResponseStream();
		Capabilities capabilities = (Capabilities) um.unmarshal(is);
		
		catalogService.setProvider(capabilities.getServiceProvider().getProviderName());
		catalogService.setLastcrawling(new Timestamp(System.currentTimeMillis()));
		catalogService.setTextdescription(capabilities.getServiceIdentification().getAbstract());
		catalogService.setUrl(cswServer.getURL().toString());
		
		csd.save(catalogService);
				
		final GetRecordsRequest getRecords = cswServer.createGetRecords();
		getRecords.setTypeNames("gmd:MD_Metadata");
		getRecords.setConstraintLanguage("CQL");
		getRecords.setConstraintLanguageVersion("1.1.0");
		getRecords.setConstraint("Title like '%'");
		is = getRecords.getResponseStream();
		
		// unmarshall the response
		GetRecordsResponseType response = ((JAXBElement<GetRecordsResponseType>) um.unmarshal(is)).getValue();
		// print the number of result matching the request
		System.out.println(response.getSearchResults().getNumberOfRecordsMatched());
		/**
		 * retrieve results in dublin core
		 */
		getRecords.setResultType(ResultType.RESULTS);
		is = getRecords.getResponseStream();
		 
		
		/**
		 * retrieve results in ISO 19139
		 */
		getRecords.setOutputSchema("http://www.isotc211.org/2005/gmd");
		is = getRecords.getResponseStream();

		getRecords.setStartPosition(1);
		getRecords.setMaxRecords(1);
		is = getRecords.getResponseStream();

		int i = 0;
		int j = 0;
		
		while(j < response.getSearchResults().getNumberOfRecordsMatched()){
			is = getRecords.getResponseStream();
			
			try{
				
			response = ((JAXBElement<GetRecordsResponseType>) um.unmarshal(is)).getValue();
			Iterator iterator = response.getSearchResults().getAny().iterator();
			
			while(iterator.hasNext()){
				Metadata metadata = (Metadata) iterator.next();
				//MetadataDAO metadataDAO = new MetadataDAO();
				Metadatarecord metadatarecord = new Metadatarecord();
//				
				metadatarecord.setMetadataidentifier(metadata.getFileIdentifier());
				metadatarecord.setPublicationdate(metadata.getDateStamp());
				//Iterator key = metadata.getIdentificationInfo().iterator().next().getDescriptiveKeywords().iterator().next().getKeywords().iterator();
				//while(key.hasNext()){
					//System.out.println(key.next());
				//}
				//metadatarecord.setKeywords(metadata.getIdentificationInfo().iterator().next().getDescriptiveKeywords().iterator().next().getKeywords().toString());
				metadatarecord.setPublisher(metadata.getIdentificationInfo().iterator().next().getPointOfContacts().iterator().next().getOrganisationName().toString());
				metadatarecord.setName(metadata.getIdentificationInfo().iterator().next().getCitation().getTitle().toString());
				//metadatarecord.setRevisiondate(metadata.getIdentificationInfo().iterator().next().getCitation().getDates().iterator().next().getDate());
				metadatarecord.setTextdescription(metadata.getIdentificationInfo().iterator().next().getAbstract().toString());
				Iterator inte = metadata.getDistributionInfo().getTransferOptions().iterator().next().getOnLines().iterator();
				String url = null;
				while(inte.hasNext()){
					org.apache.sis.metadata.iso.citation.DefaultOnlineResource test= (DefaultOnlineResource) inte.next();
					url = test.getLinkage().toURL().toString();
					if(url.contains("wms")){
						System.out.println("Serviço wms");
						WebMapServer WMS = new WebMapServer(test.getLinkage().toURL());
						WMSCapabilities capabilitiesWMS =  WMS.getCapabilities();
						List<Layer> layer = capabilitiesWMS.getLayerList();
						
						System.out.println(layer.size());
						Iterator<Layer> l = layer.iterator();
						while(l.hasNext()){
							Layer la = l.next(); 
							System.out.println("TITLE:" + la.getTitle());
							System.out.println("NOME:" + la.getName());
							System.out.println("PALAVRAS CHAVES :" + Arrays.toString(la.getKeywords()));
							System.out.println("DESCIÇÃO :" + la.get_abstract());			
							System.out.println();
						}
						
					}else{
						System.out.println("Outro Serviço");
					}
					
				}
				metadatarecord.setUrl(url);
				metadatarecord.setCatalogserviceBean(catalogService);
				
				System.out.println(metadata);
				em.getTransaction().begin();
				em.persist(metadatarecord);
				em.getTransaction().commit();
				i++;
			}
			}catch(Exception e){
				//e.printStackTrace();
				//System.out.println("TENTE NOVAMENTE MAIS TARDE");
			}
			getRecords.setStartPosition(10 + getRecords.getStartPosition());
			j++;
		}

	}
	
}
