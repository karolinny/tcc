package br.edu.ifpb.servicos;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import br.edu.ifpb.crawler.Controller;
import br.edu.ifpb.entidades.Metadatarecord;
import edu.uci.ics.crawler4j.url.WebURL;

public class VerifyService {
	private String URL;
	private WebMapServerLayers wmsLayers;
	private WebFeatureServiceLayers wfsLayers;
	private Controller crawler;
	private List<String> possiveisServiço = new ArrayList<String>();
	private Metadatarecord metadata;
	
	public VerifyService(Metadatarecord metadata){
		this.crawler = new Controller();
		this.wmsLayers = new WebMapServerLayers();
		this.wfsLayers = new WebFeatureServiceLayers();
		this.metadata = metadata;
	}
	

	public String identifyService(String url){
		
		 URL = url.toUpperCase();
		
		if(URL.contains("OWS")) return "wms-wfs";
		if(URL.contains("WMS")||URL.contains("MAPSERV")) return "wms";
		if(URL.contains("I3GEO") || URL.contains("MAP")) return "wms";
		if(URL.contains("WFS")) return "wfs";
		
		return "crawler";
	}
	
	public void sendForServiceOrCrawler(String url, URL link) throws Exception{
		if(this.identifyService(url).equals("wms-wfs")){
			this.wmsLayers.getServicoWMS(link, this.metadata);
			this.wfsLayers.setServicoWFS(link,this.metadata);
		}
		if(this.identifyService(url).equals("wms")){
			this.wmsLayers.getServicoWMS(link, this.metadata);
		}else if (this.identifyService(url).equals("wfs")){
			this.wfsLayers.setServicoWFS(link,this.metadata);
		}else if(this.identifyService(url).equals("crawler")){
			 this.crawler.callOfCrawler(url);
			 this.getMetadataCrawler(this.metadata);
		}
	}
	
	public void getMetadataCrawler(Metadatarecord metadataAtual){
		EntityManager em = Persistence.createEntityManagerFactory("tcc-up").createEntityManager();
        int idUltimometadata = (int)em.createQuery("SELECT MAX(m.id) FROM Metadatarecord m ").getSingleResult();
        Metadatarecord metadatanova = em.find(Metadatarecord.class, idUltimometadata);
        
        if(metadatanova.getMetadataidentifier() != metadataAtual.getMetadataidentifier()){
        
        metadatanova.setCatalogserviceBean(metadataAtual.getCatalogserviceBean());
        metadatanova.setKeywords(metadataAtual.getKeywords());
        metadatanova.setMetadataidentifier(metadataAtual.getMetadataidentifier());
        metadatanova.setMetadatarecordurls(metadataAtual.getMetadatarecordurls());
        metadatanova.setName(metadataAtual.getName());
        metadatanova.setPublisher(metadataAtual.getPublisher());
        metadatanova.setTextdescription(metadataAtual.getTextdescription());
        
        
		em.clear();
	
		em.getTransaction().begin();
        em.merge(metadatanova);
        em.getTransaction().commit();
        em.close();
        }
	}
	
	public void linkCrawler(List<Object> urlss){
		
	Iterator i = urlss.iterator();
	
	while(i.hasNext()){
         	WebURL link = (WebURL) i.next();
         	if(link.getURL().contains("ows")||link.getURL().contains("MAPSERV")) {
         		possiveisServiço.add(link.getURL());
         		System.out.println("-----------URL SERVIÇO --------------");
         		
         	}
     		//if(URL.contains("I3GEO") || URL.contains("MAP")) return "wms";
     		//if(URL.contains("WFS")) return "wfs";
         	System.out.println("Links da pagina: " + link.getURL());
         }
        if(!possiveisServiço.isEmpty()){
     	   try {
				this.trataServiçoDoCrawler(this.metadata, possiveisServiço);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
	
	public void trataServiçoDoCrawler(Metadatarecord metadata, List<String> servicos) throws Exception{
		System.out.println("Entrei n o serviço trataServiço");
		Iterator i = servicos.iterator();
		
		while(i.hasNext()){
			String servico = (String) i.next();
			String [] partUrl = servico.split("?");
			URL link = new URL(partUrl[1]);
			this.sendForServiceOrCrawler(partUrl[1], link);
		}
	}
	

}
