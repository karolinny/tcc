package br.edu.ifpb.test;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.geotoolkit.client.CapabilitiesException;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.ows.ServiceException;

public class App {

	public static void main(String[] args) throws CapabilitiesException, IOException, JAXBException, ServiceException {
		//http://www.geoservicos.ibge.gov.br/geoserver/wms
		URL URL =  URL = new URL("http://www.geoservicos.ibge.gov.br/geoserver/wms?");

		WebMapServer WMS = new WebMapServer(URL);
		WMSCapabilities capabilities =  WMS.getCapabilities();

		List<Layer> layer = capabilities.getLayerList();
		
		System.out.println(layer.size());
		Iterator<Layer> l = layer.iterator();
		
		while(l.hasNext()){
			Layer la = l.next(); 
			System.out.println("TITLE:" + la.getTitle());
			System.out.println("NOME:" + la.getName());
			System.out.println("PALAVRAS CHAVES :" + Arrays.toString(la.getKeywords()));
			System.out.println("DESCIÇÃO :" + la.get_abstract());			
			System.out.println();
			
		}
			
	
	}
}
