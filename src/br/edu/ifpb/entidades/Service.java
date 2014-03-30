package br.edu.ifpb.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the service database table.
 * 
 */
@Entity
@NamedQuery(name="Service.findAll", query="SELECT s FROM Service s")
public class Service implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private Timestamp begintime;

	private Timestamp endtime;

	private String keywords;

	private String metadataidentifier;

	private String name;

	private String publisher;

	private String textdescription;

	private String update;

	private String url;

	//bi-directional many-to-one association to Featuretype
	@OneToMany(mappedBy="serviceBean")
	private List<Featuretype> featuretypes;

	//bi-directional many-to-one association to Metadatarecord
	@ManyToOne
	@JoinColumn(name="metadatarecord")
	private Metadatarecord metadatarecordBean;

	public Service() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getBegintime() {
		return this.begintime;
	}

	public void setBegintime(Timestamp begintime) {
		this.begintime = begintime;
	}

	public Timestamp getEndtime() {
		return this.endtime;
	}

	public void setEndtime(Timestamp endtime) {
		this.endtime = endtime;
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

	public String getPublisher() {
		return this.publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
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

	public List<Featuretype> getFeaturetypes() {
		return this.featuretypes;
	}

	public void setFeaturetypes(List<Featuretype> featuretypes) {
		this.featuretypes = featuretypes;
	}

	public Featuretype addFeaturetype(Featuretype featuretype) {
		getFeaturetypes().add(featuretype);
		featuretype.setServiceBean(this);

		return featuretype;
	}

	public Featuretype removeFeaturetype(Featuretype featuretype) {
		getFeaturetypes().remove(featuretype);
		featuretype.setServiceBean(null);

		return featuretype;
	}

	public Metadatarecord getMetadatarecordBean() {
		return this.metadatarecordBean;
	}

	public void setMetadatarecordBean(Metadatarecord metadatarecordBean) {
		this.metadatarecordBean = metadatarecordBean;
	}

}