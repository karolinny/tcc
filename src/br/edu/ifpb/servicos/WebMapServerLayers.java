package br.edu.ifpb.servicos;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.xml.MetadataURL;
import org.geotools.ows.ServiceException;

import br.edu.ifpb.daos.FeatureDAO;
import br.edu.ifpb.daos.ServiceDAO;
import br.edu.ifpb.entidades.Featuretype;
import br.edu.ifpb.entidades.Metadatarecord;
import br.edu.ifpb.entidades.Service;

public class WebMapServerLayers {
	private Service service;
	private Featuretype feature;

	public void getLayers(Iterator<Layer> layers, Service service, WMSCapabilities capabilities) {
		System.out.println("==================CAMADA DO SERVIÇO==============================");
		while (layers.hasNext()) {
			feature = new Featuretype();
			Layer layer = layers.next();
			
			System.out.println("TITLE:" + layer.getTitle());
			feature.setTitle(layer.getTitle());
			System.out.println("NOME:" + layer.getName());
			feature.setName(layer.getName());
			System.out.println("PALAVRAS CHAVES :"
					+ Arrays.toString(layer.getKeywords()));
			feature.setKeywords(Arrays.toString(layer.getKeywords()));
			System.out.println("DESCIÇÃO :" + layer.get_abstract());
			feature.setTextdescription(layer.get_abstract());
			feature.setServiceBean(service);
			feature.setFormat(capabilities.getRequest().getGetFeatureInfo().getFormats().get(0));
			
			System.out.println();
			System.out.println();
			saveFeature(feature);
			List<MetadataURL> servicoWMS = layer.getMetadataURL();
			//System.out.println("Quantidade de links dos serviço: "	+ servicoWMS.size());
			Iterator<MetadataURL> urlServicoWms = servicoWMS.iterator();

			this.getUrlByLayer(urlServicoWms);
		}
	}

	public void getUrlByLayer(Iterator<MetadataURL> urlMetadata) {
		while (urlMetadata.hasNext()) {
			MetadataURL m = urlMetadata.next();
			System.out.println("URL do serviço WMS: " + m.getUrl());
		}
	}

	public void getServicoWMS(URL url, Metadatarecord metadata) throws ServiceException, IOException {
		
		WebMapServer WMS = new WebMapServer(url);
		WMSCapabilities capabilitiesWMS = WMS.getCapabilities();
		List<Layer> layer = capabilitiesWMS.getLayerList();
		service = new Service();
		System.out.println("======================SERVIÇO WMS=================================");
		service.setName(capabilitiesWMS.getService().getName().toString());
		service.setTextdescription(capabilitiesWMS.getService().get_abstract());
		service.setKeywords(Arrays.toString(capabilitiesWMS.getService().getKeywordList()));
		service.setPublisher(capabilitiesWMS.getService().getContactInformation().getOrganisationName().toString());
		service.setUrl(capabilitiesWMS.getService().getOnlineResource().getPath());
		service.setMetadatarecordBean(metadata);
		service.setMetadataidentifier(metadata.getMetadataidentifier());
		saveService(service);
		System.out.println("Quantidades de links de camadas:" + layer.size());
		Iterator<Layer> l = layer.iterator();
		this.getLayers(l,this.service,capabilitiesWMS);
		System.out.println("==================================================================");
		System.out.println();
		System.out.println();

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
