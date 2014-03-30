package br.edu.ifpb.entidades;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.sleepycat.persist.model.Entity;


/**
 * The persistent class for the catalogservice database table.
 * 
 */
@javax.persistence.Entity
@NamedQuery(name="Catalogservice.findAll", query="SELECT c FROM Catalogservice c")
public class Catalogservice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private Timestamp lastcrawling;

	private String provider;

	private String textdescription;

	private String url;

	//bi-directional many-to-one association to Metadatarecord
	@OneToMany(mappedBy="catalogserviceBean")
	private List<Metadatarecord> metadatarecords;

	public Catalogservice() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getLastcrawling() {
		return this.lastcrawling;
	}

	public void setLastcrawling(Timestamp lastcrawling) {
		this.lastcrawling = lastcrawling;
	}

	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getTextdescription() {
		return this.textdescription;
	}

	public void setTextdescription(String textdescription) {
		this.textdescription = textdescription;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Metadatarecord> getMetadatarecords() {
		return this.metadatarecords;
	}

	public void setMetadatarecords(List<Metadatarecord> metadatarecords) {
		this.metadatarecords = metadatarecords;
	}

	public Metadatarecord addMetadatarecord(Metadatarecord metadatarecord) {
		getMetadatarecords().add(metadatarecord);
		metadatarecord.setCatalogserviceBean(this);

		return metadatarecord;
	}

	public Metadatarecord removeMetadatarecord(Metadatarecord metadatarecord) {
		getMetadatarecords().remove(metadatarecord);
		metadatarecord.setCatalogserviceBean(null);

		return metadatarecord;
	}

}