package br.edu.ifpb.servicos;

import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.geotoolkit.data.wfs.WebFeatureServer;
import org.geotoolkit.wfs.xml.FeatureType;
import org.geotoolkit.wfs.xml.FeatureTypeList;
import org.geotoolkit.wfs.xml.WFSCapabilities;
import org.geotoolkit.wfs.xml.v100.FeatureTypeType;
import org.geotoolkit.wfs.xml.v100.LatLongBoundingBoxType;
import org.geotoolkit.wfs.xml.v100.MetadataURLType;
import org.geotoolkit.wms.xml.AbstractKeyword;

import br.edu.ifpb.daos.FeatureDAO;
import br.edu.ifpb.daos.ServiceDAO;
import br.edu.ifpb.entidades.Featuretype;
import br.edu.ifpb.entidades.Metadatarecord;
import br.edu.ifpb.entidades.Service;

public class WebFeatureServiceLayers {
	private Service service;
	private Featuretype features;
	
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
	
	public void saveService(Service service){
		ServiceDAO servicedao = new ServiceDAO();
		servicedao.save(service);
	}
	
	public void saveFeature(Featuretype feature){
		FeatureDAO featuredao = new FeatureDAO();
		featuredao.save(feature);
	}

}
