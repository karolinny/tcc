package br.edu.ifpb.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.batik.svggen.font.table.Coverage;
import org.geotoolkit.wcs.WebCoverageServer;
import org.geotoolkit.wcs.xml.CoverageInfo;
import org.geotoolkit.wcs.xml.WCSVersion;
import org.geotoolkit.wcs.xml.v100.WCSCapabilitiesType;
import org.geotoolkit.wcs.xml.v100.WCSCapabilityType;
import org.geotoolkit.wcs.xml.v111.CoverageDescriptionType;
import org.geotoolkit.wcs.xml.v111.CoverageDescriptions;
import org.geotools.arcsde.session.Commands.GetVersionCommand;
import org.geotools.wcs.WCS;

public class WebCoverageServerApp {
	public static void main(String[] args) throws MalformedURLException {
		
		//http://nsidc.org/cgi-bin/atlas_north?service=WCS&request=GetCapabilities&version=1.1.1
		//http://nsidc.org/cgi-bin/atlas_south?service=WCS&request=GetCapabilities&version=1.1.1
			
		final WebCoverageServer wcsServer = new WebCoverageServer(new URL("http://www.geoservicos.ibge.gov.br/geoserver/wcs?" ), null, WCSVersion.v111);
		WCSCapabilitiesType capaWCS =  wcsServer.getCapabilities();
		
		CoverageDescriptions d = (CoverageDescriptions) capaWCS.getContents();
		List<CoverageDescriptionType> de = d.getCoverageDescription();
		
		//List<? extends CoverageInfo> c =capaWCS.getContents().getCoverageInfos()
		Iterator it = de.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
			
			
		}
	}
}
