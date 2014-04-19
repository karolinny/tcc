package br.edu.ifpb.servicos;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.geotoolkit.data.wfs.WebFeatureServer;
import org.geotoolkit.wfs.xml.FeatureType;
import org.geotoolkit.wfs.xml.FeatureTypeList;
import org.geotoolkit.wfs.xml.WFSCapabilities;
import org.geotoolkit.wfs.xml.v100.FeatureTypeType;
import org.geotoolkit.wfs.xml.v100.LatLongBoundingBoxType;
import org.geotoolkit.wfs.xml.v100.MetadataURLType;
import org.geotoolkit.wms.xml.AbstractKeyword;
import org.geotoolkit.ows.xml.v100.KeywordsType;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.xml.MetadataURL;
import org.geotools.ows.ServiceException;

import br.edu.ifpb.crawler.Controller;
import br.edu.ifpb.daos.FeatureDAO;
import br.edu.ifpb.daos.ServiceDAO;
import br.edu.ifpb.entidades.Featuretype;
import br.edu.ifpb.entidades.Metadatarecord;
import br.edu.ifpb.entidades.Service;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class TesteComOCrawler extends WebCrawler{
	
	private String URL;
	private Service service;
	private Featuretype feature, features;
	private Metadatarecord metadata = new Metadatarecord();
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("tcc-up");
	private static EntityManager em = emf.createEntityManager();
		
	private List<String> possiveisServiço = new ArrayList<String>();
	
	public String identifyService(String url){
		
		 URL = url.toUpperCase();
		
		if(URL.contains("OWS")) return "wms-wfs";
		if(URL.contains("WMS")||URL.contains("MAPSERV")) return "wms";
		if(URL.contains("I3GEO") || URL.contains("MAP")) return "wms";
		if(URL.contains("WFS")) return "wfs";
		
		return "crawler";
	}
	
	public void sendForServiceOrCrawler(String url, URL link, Metadatarecord metadata) throws Exception{
		
		System.out.println("Identificando serviços");
		System.out.println("URL: " + url +"\n" + "Link" + link);
		if(this.identifyService(url).equals("wms-wfs")){
			this.getServicoWMS(link, metadata);
			this.setServicoWFS(link, metadata);
		}
		if(this.identifyService(url).equals("wms")){
			this.getServicoWMS(link, metadata);
		}else if (this.identifyService(url).equals("wfs")){
			this.setServicoWFS(link,metadata);
		}
	}
	
	public void linkCrawler(List<WebURL> urlss){
		
		
	Iterator i = urlss.iterator();
	
	while(i.hasNext()){
        	WebURL link = (WebURL) i.next();
        	if(link.getURL().contains("ows")||link.getURL().contains("MAPSERV")) {
        		possiveisServiço.add(link.getURL());
        		System.out.println("-----------URL SERVIÇO --------------");
        		metadata.setMetadataidentifier("1");
        	}
    		
        	//System.out.println("Links da pagina: " + link.getURL());
        }
       if(!possiveisServiço.isEmpty()){
    	   try {
    		   System.out.println("Envia para o trata serviço");
				this.trataServiçoDoCrawler(possiveisServiço);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
	
	public void trataServiçoDoCrawler(List<String> servicos) throws Exception{
		System.out.println("Entrei n o serviço trataServiço");
		Iterator i = servicos.iterator();
		
		while(i.hasNext()){
			String servico = (String) i.next();
			String [] partUrl = servico.split("\\?");
			URL link = new URL(partUrl[0] + "?");
			this.sendForServiceOrCrawler(partUrl[0]+ "?", link, this.metadata);
		}
	}
	


	@Override
    public void visit(Page page) {          
            String url = page.getWebURL().getURL();
            System.out.println("URL: " + url);

            if (page.getParseData() instanceof HtmlParseData) {
                    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                    String html = htmlParseData.getHtml();
                    List<WebURL> links = htmlParseData.getOutgoingUrls();
                    
                    linkCrawler(links);
                   
                    
            }
    }
	
	public void getLayers(Iterator<Layer> layers, Service service, WMSCapabilities capabilities) {
		System.out.println("==================CAMADA DO SERVIÇO==============================");
		while (layers.hasNext()) {
			feature = new Featuretype();
			Layer layer = layers.next();
			
			System.out.println("TITLE:" + layer.getTitle());
			feature.setTitle(layer.getTitle());
			System.out.println("NOME:" + layer.getName());
			feature.setName(layer.getName());
			System.out.println("PALAVRAS CHAVES :"
					+ Arrays.toString(layer.getKeywords()));
			feature.setKeywords(Arrays.toString(layer.getKeywords()));
			System.out.println("DESCIÇÃO :" + layer.get_abstract());
			feature.setTextdescription(layer.get_abstract());
			feature.setServiceBean(service);
			feature.setFormat(capabilities.getRequest().getGetFeatureInfo().getFormats().get(0));
			
			System.out.println();
			System.out.println();
			saveFeature(feature);
			List<MetadataURL> servicoWMS = layer.getMetadataURL();
			//System.out.println("Quantidade de links dos serviço: "	+ servicoWMS.size());
			Iterator<MetadataURL> urlServicoWms = servicoWMS.iterator();

			this.getUrlByLayer(urlServicoWms);
		}
	}

	public void getUrlByLayer(Iterator<MetadataURL> urlMetadata) {
		while (urlMetadata.hasNext()) {
			MetadataURL m = urlMetadata.next();
			System.out.println("URL do serviço WMS: " + m.getUrl());
		}
	}

	public void getServicoWMS(URL url, Metadatarecord metadata) throws ServiceException, IOException {
		
		WebMapServer WMS = new WebMapServer(url);
		WMSCapabilities capabilitiesWMS = WMS.getCapabilities();
		List<Layer> layer = capabilitiesWMS.getLayerList();
		service = new Service();
		System.out.println("======================SERVIÇO WMS=================================");
		service.setName(capabilitiesWMS.getService().getName().toString());
		service.setTextdescription(capabilitiesWMS.getService().get_abstract());
		service.setKeywords(Arrays.toString(capabilitiesWMS.getService().getKeywordList()));
		service.setPublisher(capabilitiesWMS.getService().getContactInformation().getOrganisationName().toString());
		service.setUrl(capabilitiesWMS.getService().getOnlineResource().getPath());
		service.setMetadatarecordBean(metadata);
		service.setMetadataidentifier(metadata.getMetadataidentifier());
		saveService(service);
		System.out.println("Quantidades de links de camadas:" + layer.size());
		Iterator<Layer> l = layer.iterator();
		this.getLayers(l,this.service,capabilitiesWMS);
		System.out.println("==================================================================");
		System.out.println();
		System.out.println();

	}
	
	
	
	public void saveService(Service service){
		 EntityManagerFactory emf = Persistence.createEntityManagerFactory("tcc-up");
		 EntityManager em = null;
		
		
			
			em = emf.createEntityManager();
			em.clear();
		
			em.getTransaction().begin();
			em.merge(service);
			//em.flush();
			em.getTransaction().commit();
//			em.close();
		
	}
	
	public void saveFeature(Featuretype feature){
//		EntityManagerFactory emf = Persistence.createEntityManagerFactory("tcc-up");
//		 EntityManager em = null;
//		
//		
//			
//			em = emf.createEntityManager();
//			em.clear();
		
			em.getTransaction().begin();
			em.merge(feature);
			//em.flush();
			em.getTransaction().commit();
//			em.close();
		
	}
	
	public void setServicoWFS(URL url,Metadatarecord metadata){
		final WebFeatureServer wfsServer = new WebFeatureServer(url, "1.1.0");
		WFSCapabilities capa = wfsServer.getCapabilities();
		
		service = new Service();
		service.setName(capa.getServiceProvider().getProviderName());
		Iterator key = capa.getServiceIdentification().getKeywords().iterator();
		int i = 0;
		String [] palavrasChave = new String [capa.getServiceIdentification().getKeywords().size()];
		while(key.hasNext()){
			AbstractKeyword palavras =  (AbstractKeyword) key.next();
			 
			
				palavrasChave[i++] = palavras.getValue();
			}
		
		service.setKeywords(Arrays.toString(palavrasChave));
		service.setTextdescription(capa.getServiceIdentification().getFirstAbstract());
		saveService(service);
		
		FeatureTypeList f = (FeatureTypeList) capa.getFeatureTypeList();
		Iterator<? extends FeatureType> layers = f.getFeatureType().iterator();
		
		this.getLayers(layers, service);
	}
	
	public void getLayers(Iterator<? extends FeatureType> layers, Service service){
		while(layers.hasNext()){
			FeatureType feature =  (FeatureType) layers.next();
			
			@SuppressWarnings("unchecked")
			FeatureTypeType fet = new FeatureTypeType(feature.getName(), feature.getDefaultCRS(), null, (List<LatLongBoundingBoxType>) feature.getBoundingBox());
			System.out.println("TITLE: " + fet.getTitle());
			features.setTitle( fet.getTitle());
			System.out.println("ABSTRACT: " + fet.getAbstract());
			features.setTextdescription(fet.getAbstract());
			System.out.println("KEYWORDS: " + fet.getKeywords());
			features.setKeywords(fet.getKeywords());
			
			saveFeature(features);
			List<MetadataURLType> servicoWFS = fet.getMetadataURL();
			System.out.println("Quantidade de links dos serviço: "
					+ servicoWFS.size());
			Iterator<MetadataURLType> urlServicoWFS = servicoWFS.iterator();
			
			this.setUrlLayer(urlServicoWFS);
		}
	}
	
	public void setUrlLayer(Iterator<MetadataURLType> urlMetadata){
		while (urlMetadata.hasNext()) {
			MetadataURLType m = urlMetadata.next();
			System.out.println("URL do serviço WFS: " + m.getValue());
		}
	}
	public void setMetadata(){
		
	}
}
