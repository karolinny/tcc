package br.edu.ifpb.entidades;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the featuretype database table.
 * 
 */
@Entity
@NamedQuery(name="Featuretype.findAll", query="SELECT f FROM Featuretype f")
public class Featuretype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private Timestamp begintime;

	private Timestamp endtime;

	private String format;

	private String keywords;

	private String metadataidentifier;

	private String name;

	private String textdescription;

	private String title;

	private String update;

	//bi-directional many-to-one association to Service
	@ManyToOne
	@JoinColumn(name="service")
	private Service serviceBean;

	public Featuretype() {
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

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
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

	public String getTextdescription() {
		return this.textdescription;
	}

	public void setTextdescription(String textdescription) {
		this.textdescription = textdescription;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUpdate() {
		return this.update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public Service getServiceBean() {
		return this.serviceBean;
	}

	public void setServiceBean(Service serviceBean) {
		this.serviceBean = serviceBean;
	}

}