package br.edu.ifpb.servicos;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.geotoolkit.data.wfs.WebFeatureServer;
import org.geotoolkit.wfs.xml.FeatureType;
import org.geotoolkit.wfs.xml.FeatureTypeList;
import org.geotoolkit.wfs.xml.WFSCapabilities;
import org.geotoolkit.wfs.xml.v100.FeatureTypeType;
import org.geotoolkit.wfs.xml.v100.LatLongBoundingBoxType;
import org.geotoolkit.wfs.xml.v100.MetadataURLType;

import br.edu.ifpb.entidades.Service;

public class WebFeatureServiceLayers {
	private Service service;
	
	public void setServicoWFS(URL url){
		final WebFeatureServer wfsServer = new WebFeatureServer(url, "1.1.0");
		WFSCapabilities capa = wfsServer.getCapabilities();
		
		FeatureTypeList f = (FeatureTypeList) capa.getFeatureTypeList();
		Iterator<? extends FeatureType> layers = f.getFeatureType().iterator();
		
		this.getLayers(layers);
	}
	
	public void getLayers(Iterator<? extends FeatureType> layers){
		while(layers.hasNext()){
			FeatureType feature =  (FeatureType) layers.next();
			
			@SuppressWarnings("unchecked")
			FeatureTypeType fet = new FeatureTypeType(feature.getName(), feature.getDefaultCRS(), null, (List<LatLongBoundingBoxType>) feature.getBoundingBox());
			System.out.println("TITLE: " + fet.getTitle());
			System.out.println("ABSTRACT: " + fet.getAbstract());
			System.out.println("KEYWORDS: " + fet.getKeywords());
			
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
}
