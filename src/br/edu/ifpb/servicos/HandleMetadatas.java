package br.edu.ifpb.servicos;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.sis.metadata.iso.citation.DefaultOnlineResource;
import org.opengis.metadata.Metadata;

import br.edu.ifpb.crawler.Controller;
import br.edu.ifpb.entidades.Metadatarecord;
import br.edu.ifpb.entidades.Metadatarecordurl;

public class HandleMetadatas {
	private String url;
	private URL link;
	private VerifyService verifyService;
	private WebMapServerLayers wmsLayers;
	private WebFeatureServiceLayers wfsLayers;
	private Controller crawler;
	private Metadatarecord metadataRecord;
	private List<Metadatarecordurl> metadataRecordUrl;
	
	public HandleMetadatas(){
		this.verifyService = new VerifyService();
		this.wmsLayers = new WebMapServerLayers();
		this.wfsLayers = new WebFeatureServiceLayers();
		this.metadataRecord = new Metadatarecord();
		this.metadataRecordUrl = new ArrayList<Metadatarecordurl>();
		this.crawler = new Controller();
		this.url = null;
		this.link = null;
	}

	
	public void setInformationFromMetadados(Iterator listMetadados) throws Exception{
		Metadata metadata = (Metadata) listMetadados.next();
		
		System.out.println("IDENTIFICAÇÃO DO METADADO: " + metadata.getFileIdentifier());
		System.out.println("DATA DA CRIAÇÃO: " + metadata.getDateStamp());
		System.out.println("NOME DA ORGANIZAÇÃO: " + metadata.getIdentificationInfo().iterator().next().getPointOfContacts().iterator().next().getOrganisationName().toString());
		System.out.println("TÍTULO: " + metadata.getIdentificationInfo().iterator().next().getCitation().getTitle().toString());
		System.out.println("RESUMO: " + metadata.getIdentificationInfo().iterator().next().getAbstract().toString());
		
		Iterator listLinks = metadata.getDistributionInfo().getTransferOptions().iterator().next().getOnLines().iterator();
		this.setListOfLinksByMetadados(listLinks);
	}
	
	public void setListOfLinksByMetadados(Iterator listLinks) throws Exception{
		while(listLinks.hasNext()){
			DefaultOnlineResource test= (DefaultOnlineResource) listLinks.next();
			
			this.url = test.getLinkage().toURL().toString();
			this.link = test.getLinkage().toURL();
			
			System.out.println("URL: " + url);
			
			if(verifyService.identifyService(this.url).equals("wms")){
				this.wmsLayers.getServicoWMS(this.link);
			}else if (verifyService.identifyService(this.url).equals("wfs")){
				this.wfsLayers.setServicoWFS(this.link);
			}else if(verifyService.identifyService(this.url).equals("crawler")){
				 this.crawler.callOfCrawler(this.url);
			}
		}
	}
		

}
