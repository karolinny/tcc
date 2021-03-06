package br.edu.ifpb.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.sis.metadata.iso.citation.DefaultOnlineResource;
import org.apache.sis.xml.MarshallerPool;
import org.geotoolkit.csw.CatalogServicesServer;
import org.geotoolkit.csw.GetCapabilitiesRequest;
import org.geotoolkit.csw.GetRecordsRequest;
import org.geotoolkit.csw.xml.CSWMarshallerPool;
import org.geotoolkit.csw.xml.ResultType;
import org.geotoolkit.csw.xml.v202.Capabilities;
import org.geotoolkit.csw.xml.v202.GetRecordsResponseType;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.xml.MetadataURL;
import org.opengis.metadata.Metadata;

import br.edu.ifpb.crawler.CrawlerPag;
import br.edu.ifpb.servicos.HandleMetadatas;
import br.edu.ifpb.servicos.VerifyService;
import br.edu.ifpb.servicos.WebMapServerLayers;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class BuscandoServicos {
	
	private static String inde = "http://www.metadados.inde.gov.br/geonetwork/srv/br/csw?";
	private static String ana = "http://metadados.ana.gov.br/geonetwork/srv/pt/csw?";
	private static String ideandalucia = "http://www.ideandalucia.es/catalogodeservicios/srv/es/csw?";
	private static String idee = "http://www.idee.es/csw-inspire-idee/srv/eng/csw?";
	private static String geoportaligm = "http://www.geoportaligm.gob.ec/geonetwork/srv/es/csw?";
	private static int PROFUNDIDADE = 7;
	private static String aesa ="http://geo.aesa.pb.gov.br/csw?";

	public static void main(String[] args) throws MalformedURLException, URISyntaxException, IOException, JAXBException {
		//VerifyService verifica = new VerifyService(); 
		//WebMapServerLayers wmsi = new WebMapServerLayers();
		HandleMetadatas getMetadados = new HandleMetadatas();
		
		final MarshallerPool marshallerPool = CSWMarshallerPool.getInstance();
		final Unmarshaller unmarshaller = marshallerPool.acquireUnmarshaller();
		
		//Chamada ao catalogo de serviço de uma IDE(SDI)
		final CatalogServicesServer cswServer = new CatalogServicesServer(new URL(geoportaligm),"2.0.2");
		
		//Requisição do getCapabilities
		final GetCapabilitiesRequest getCapa = cswServer.createGetCapabilities();
		InputStream is = getCapa.getResponseStream();
		Capabilities capabilities = (Capabilities) unmarshaller.unmarshal(is);
		
		//Requisição dos registros
		final GetRecordsRequest getRecords = cswServer.createGetRecords();
		getRecords.setTypeNames("gmd:MD_Metadata");
		getRecords.setConstraintLanguage("CQL");
		getRecords.setConstraintLanguageVersion("1.1.0");
		getRecords.setConstraint("Title like '%'");
		is = getRecords.getResponseStream();
		
		// unmarshall a resposta
		GetRecordsResponseType response = ((JAXBElement<GetRecordsResponseType>) unmarshaller.unmarshal(is)).getValue();
		System.out.println("Número de resultado encontrados: " + response.getSearchResults().getNumberOfRecordsMatched());
		
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
		
		
		int j = 0;
		
		while(j < response.getSearchResults().getNumberOfRecordsMatched()){
			is = getRecords.getResponseStream();
			
			try{
				
			response = ((JAXBElement<GetRecordsResponseType>) unmarshaller.unmarshal(is)).getValue();
			Iterator iterator = response.getSearchResults().getAny().iterator();
			//getMetadados.saveMetadata(listMetadados, catalog);(iterator);

			}catch(Exception e){
				//e.printStackTrace();
			}
			getRecords.setStartPosition(10 + getRecords.getStartPosition());
			j++;
		}
		
	}
}
