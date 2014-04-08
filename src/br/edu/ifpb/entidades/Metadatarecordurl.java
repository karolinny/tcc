package br.edu.ifpb.entidades;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.engine.internal.Cascade;


/**
 * The persistent class for the metadatarecordurl database table.
 * 
 */
@Entity
@NamedQuery(name="Metadatarecordurl.findAll", query="SELECT m FROM Metadatarecordurl m")
public class Metadatarecordurl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String url;

	//bi-directional many-to-one association to Metadatarecord
	@ManyToOne(cascade=CascadeType.ALL)
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