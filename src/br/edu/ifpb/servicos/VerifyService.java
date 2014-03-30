package br.edu.ifpb.servicos;

import java.net.URL;

public class VerifyService {
	private String URL;

	public String identifyService(String url){
		
		 URL = url.toUpperCase();
		
		if(URL.contains("WMS")||URL.contains("MAPSERV")) return "wms";
		if(URL.contains("I3GEO") || URL.contains("MAP")) return "wms";
		if(URL.contains("WFS")) return "wfs";
		
		return "crawler";
	}
}
