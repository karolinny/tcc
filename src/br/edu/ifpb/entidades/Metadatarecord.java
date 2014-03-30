package br.edu.ifpb.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the metadatarecord database table.
 * 
 */
@Entity
@NamedQuery(name="Metadatarecord.findAll", query="SELECT m FROM Metadatarecord m")
public class Metadatarecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String keywords;

	private String metadataidentifier;

	private String name;

	@Temporal(TemporalType.DATE)
	private Date publicationdate;

	private String publisher;

	@Temporal(TemporalType.DATE)
	private Date reationdate;

	@Temporal(TemporalType.DATE)
	private Date revisiondate;

	private String temporalextent;

	private String textdescription;

	private String update;

	private String url;

	//bi-directional many-to-one association to Catalogservice
	@ManyToOne
	@JoinColumn(name="catalogservice")
	private Catalogservice catalogserviceBean;

	//bi-directional many-to-one association to Metadatarecordurl
	@OneToMany(mappedBy="metadatarecord")
	private List<Metadatarecordurl> metadatarecordurls;

	//bi-directional many-to-one association to Service
	@OneToMany(mappedBy="metadatarecordBean")
	private List<Service> services;

	public Metadatarecord() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getMetadataidentifier() {
		return this.metadataidentifier;
	}

	public void setMetadataidentifier(String metadataidentifier) {
		this.metadataidentifier = metadataidentifier;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getPublicationdate() {
		return this.publicationdate;
	}

	public void setPublicationdate(Date publicationdate) {
		this.publicationdate = publicationdate;
	}

	public String getPublisher() {
		return this.publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Date getReationdate() {
		return this.reationdate;
	}

	public void setReationdate(Date reationdate) {
		this.reationdate = reationdate;
	}

	public Date getRevisiondate() {
		return this.revisiondate;
	}

	public void setRevisiondate(Date revisiondate) {
		this.revisiondate = revisiondate;
	}

	public String getTemporalextent() {
		return this.temporalextent;
	}

	public void setTemporalextent(String temporalextent) {
		this.temporalextent = temporalextent;
	}

	public String getTextdescription() {
		return this.textdescription;
	}

	public void setTextdescription(String textdescription) {
		this.textdescription = textdescription;
	}

	public String getUpdate() {
		return this.update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Catalogservice getCatalogserviceBean() {
		return this.catalogserviceBean;
	}

	public void setCatalogserviceBean(Catalogservice catalogserviceBean) {
		this.catalogserviceBean = catalogserviceBean;
	}

	public List<Metadatarecordurl> getMetadatarecordurls() {
		return this.metadatarecordurls;
	}

	public void setMetadatarecordurls(List<Metadatarecordurl> metadatarecordurls) {
		this.metadatarecordurls = metadatarecordurls;
	}

	public Metadatarecordurl addMetadatarecordurl(Metadatarecordurl metadatarecordurl) {
		getMetadatarecordurls().add(metadatarecordurl);
		metadatarecordurl.setMetadatarecord(this);

		return metadatarecordurl;
	}

	public Metadatarecordurl removeMetadatarecordurl(Metadatarecordurl metadatarecordurl) {
		getMetadatarecordurls().remove(metadatarecordurl);
		metadatarecordurl.setMetadatarecord(null);

		return metadatarecordurl;
	}

	public List<Service> getServices() {
		return this.services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public Service addService(Service service) {
		getServices().add(service);
		service.setMetadatarecordBean(this);

		return service;
	}

	public Service removeService(Service service) {
		getServices().remove(service);
		service.setMetadatarecordBean(null);

		return service;
	}

}