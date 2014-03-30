package br.edu.ifpb.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.geotoolkit.data.wfs.WebFeatureServer;
import org.geotoolkit.wfs.xml.FeatureType;
import org.geotoolkit.wfs.xml.FeatureTypeList;
import org.geotoolkit.wfs.xml.WFSCapabilities;
import org.geotoolkit.wfs.xml.v100.FeatureTypeType;
import org.geotoolkit.wfs.xml.v100.LatLongBoundingBoxType;

public class FeatureTypeApp {
	public static void main(String[] args) throws MalformedURLException {
		//http://www.nrcan.gc.ca/earth-sciences/wfs/cubeserv.cgi?
		//http://www.geoservicos.ibge.gov.br/geoserver/wfs
		//http://nsidc.org/cgi-bin/atlas_north?service=WFS&request=GetCapabilities&version=1.1.0
		
		final WebFeatureServer wfsServer = new WebFeatureServer(new URL("http://www.geoservicos.ibge.gov.br/geoserver/wfs"), "1.1.0");
		WFSCapabilities capa = wfsServer.getCapabilities();
		
		FeatureTypeList f = (FeatureTypeList) capa.getFeatureTypeList();
		Iterator<? extends FeatureType> i = f.getFeatureType().iterator();
		
		while(i.hasNext()){
			FeatureType feature =  (FeatureType) i.next();
			
			@SuppressWarnings("unchecked")
			FeatureTypeType fet = new FeatureTypeType(feature.getName(), feature.getDefaultCRS(), null, (List<LatLongBoundingBoxType>) feature.getBoundingBox());
			System.out.println("TITLE: " + fet.getTitle());
			System.out.println("ABSTRACT: " + fet.getAbstract());
			System.out.println("KEYWORDS: " + fet.getKeywords());
			
		}
		
		
		
	}
}
