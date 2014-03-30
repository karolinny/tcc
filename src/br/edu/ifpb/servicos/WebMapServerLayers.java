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

public class WebMapServerLayers {

	public void getLayers(Iterator<Layer> layers) {
		while (layers.hasNext()) {
			Layer layer = layers.next();
			System.out.println("TITLE:" + layer.getTitle());
			System.out.println("NOME:" + layer.getName());
			System.out.println("PALAVRAS CHAVES :"
					+ Arrays.toString(layer.getKeywords()));
			System.out.println("DESCIÇÃO :" + layer.get_abstract());

			List<MetadataURL> servicoWMS = layer.getMetadataURL();
			System.out.println("Quantidade de links dos serviço: "
					+ servicoWMS.size());
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

	public void getServicoWMS(URL url) throws ServiceException, IOException {

		WebMapServer WMS = new WebMapServer(url);
		WMSCapabilities capabilitiesWMS = WMS.getCapabilities();
		List<Layer> layer = capabilitiesWMS.getLayerList();

		System.out.println("Quantidades de links de camadas:" + layer.size());
		Iterator<Layer> l = layer.iterator();
		this.getLayers(l);

		System.out.println();

	}

}
