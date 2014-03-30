package br.edu.ifpb.entidades;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the metadatarecordurl database table.
 * 
 */
@Entity
@NamedQuery(name="Metadatarecordurl.findAll", query="SELECT m FROM Metadatarecordurl m")
public class Metadatarecordurl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String url;

	//bi-directional many-to-one association to Metadatarecord
	@ManyToOne
	@JoinColumn(name="provider")
	private Metadatarecord metadatarecord;

	public Metadatarecordurl() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Metadatarecord getMetadatarecord() {
		return this.metadatarecord;
	}

	public void setMetadatarecord(Metadatarecord metadatarecord) {
		this.metadatarecord = metadatarecord;
	}

}