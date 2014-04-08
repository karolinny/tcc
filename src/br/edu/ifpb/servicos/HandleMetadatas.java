package br.edu.ifpb.servicos;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.apache.sis.metadata.iso.citation.DefaultOnlineResource;
import org.opengis.metadata.Metadata;
import org.opengis.metadata.citation.ResponsibleParty;
import org.opengis.metadata.distribution.DigitalTransferOptions;
import org.opengis.metadata.identification.Identification;

import br.edu.ifpb.daos.MetadataDAO;
import br.edu.ifpb.daos.MetadataUrlsDAO;
import br.edu.ifpb.entidades.Catalogservice;
import br.edu.ifpb.entidades.Metadatarecord;
import br.edu.ifpb.entidades.Metadatarecordurl;

public class HandleMetadatas {
	private String url;
	private URL link;
	private VerifyService verifyService;
	private Metadatarecord metadataRecord;
	private List<Metadatarecordurl> metadataRecordUrls;
	private Metadatarecordurl metatarecordurl;
	private MetadataDAO metadataDAO;
	private MetadataUrlsDAO urlsDao;
	private Metadatarecord metadata;
	
	public HandleMetadatas(){
		
		this.metadata = new Metadatarecord();
		
		this.metadataRecord = new Metadatarecord();
		this.metadataRecordUrls = new ArrayList<Metadatarecordurl>();		
		this.url = null;
		this.link = null;
	}
	
	public String getPontContato(Iterator pontContato){
			
			if(pontContato.hasNext()){
				ResponsibleParty r = (ResponsibleParty) pontContato.next();
				return r.getOrganisationName().toString();
			}
			return "unmarked";
	}
	
	public String getIdentication(Iterator identification, String tipo){
			if(identification.hasNext()){
				Identification i = (Identification) identification.next();
				switch (tipo) {
				case "contato":
					Iterator pontoContato = i.getPointOfContacts().iterator();
					return this.getPontContato(pontoContato);
				case "citation":
					return i.getCitation().getTitle().toString();
				case "abstract":
					return i.getAbstract().toString();
				}
				
			}
			return "unmarked";
			
	}

	
	public void setInformationFromMetadados(Iterator listMetadados, Catalogservice catalog) throws Exception{
		Metadata iteratorMetadata = (Metadata) listMetadados.next();
		
		this.metadata.setCatalogserviceBean(catalog);
		System.out.println("IDENTIFICAÇÃO DO METADADO: " + iteratorMetadata.getFileIdentifier());
		this.metadata.setMetadataidentifier(iteratorMetadata.getFileIdentifier());
		
		System.out.println("DATA DA CRIAÇÃO: " + iteratorMetadata.getDateStamp());
		this.metadata.setPublicationdate(iteratorMetadata.getDateStamp());
		
		Iterator identification = iteratorMetadata.getIdentificationInfo().iterator();
		System.out.println("NOME DA ORGANIZAÇÃO: " + this.getIdentication(identification,"contato"));
		this.metadata.setPublisher(this.getIdentication(identification, "contato"));
		
		System.out.println("TÍTULO: " + this.getIdentication(identification, "citation"));
		this.metadata.setName(this.getIdentication(identification, "citation"));
		
		System.out.println("RESUMO: " + this.getIdentication(identification, "abstract"));
		this.metadata.setTextdescription(this.getIdentication(identification, "abstract"));
		
		this.metadataDAO = new MetadataDAO();
		this.metadataDAO.save(this.getMetadata());
		
		this.verifyService = new VerifyService(this.metadata);
		Iterator listLinks = iteratorMetadata.getDistributionInfo().getTransferOptions().iterator();
		while(listLinks.hasNext()){
			DigitalTransferOptions obj = (DigitalTransferOptions) listLinks.next();
			Iterator links = obj.getOnLines().iterator();
			this.setListOfLinksByMetadados(links, this.metadataRecord);
		}
		this.metadata.setMetadatarecordurls(this.metadataRecordUrls);
		
		
			
				
	}
	
	public void linksMetadados(Metadata metadata) throws Exception{
	}

	
	public Metadatarecord getMetadata(){
		return this.metadata;
	}
	
	public void setListOfLinksByMetadados(Iterator listLinks, Metadatarecord metadata) throws Exception{
		
		while(listLinks.hasNext()){
			DefaultOnlineResource test= (DefaultOnlineResource) listLinks.next();
		
			this.metatarecordurl = new Metadatarecordurl();
			this.metatarecordurl.setMetadatarecord(this.getMetadata());
			if(test.getLinkage().toURL().toString()!=null){
				this.url = test.getLinkage().toURL().toString();
				this.link = test.getLinkage().toURL();
				this.metatarecordurl.setUrl(url);
				System.out.println("URL: " + url);
			}else {this.metatarecordurl.setUrl("Empty");}
			
			this.metadataRecordUrls.add(this.metatarecordurl);
			
		
			System.out.println("total de links no metadataRecordssUrl:"+ this.metadataRecordUrls.size());
			verifyService.sendForServiceOrCrawler( this.url, this.link);
		}
		this.saveMetadataRecordUrls(this.metadataRecordUrls);
	}
	
	
	public void saveMetadata(Iterator listMetadados, Catalogservice catalog){
		try {
			System.out.println("==============INFORMAÇÃO DO METADADO================================");
			this.setInformationFromMetadados(listMetadados, catalog);
			System.out.println("=====================FIM DO METADADO================================");
			System.out.println();
			System.out.println();
			this.metadataDAO = new MetadataDAO();
			this.metadataDAO.save(this.getMetadata());
			//this.saveMetadataRecordUrls(this.getMetadata().getMetadatarecordurls());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveMetadataRecordUrls(List<Metadatarecordurl> metadados){
	
			Iterator i = metadados.iterator();
			while(i.hasNext()){
				this.urlsDao  = new MetadataUrlsDAO();				
				this.urlsDao.save((Metadatarecordurl)i.next());
			}
		
	}
	
	
		

}
