package br.edu.ifpb.servicos;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.xml.MetadataURL;
import org.geotools.ows.ServiceException;

import br.edu.ifpb.crawler.Controller;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class TesteComOCrawler extends WebCrawler{
	
	private String URL;
	
	private List<String> possiveisServiço = new ArrayList<String>();
	
	public String identifyService(String url){
		
		 URL = url.toUpperCase();
		
		if(URL.contains("OWS")) return "wms-wfs";
		if(URL.contains("WMS")||URL.contains("MAPSERV")) return "wms";
		if(URL.contains("I3GEO") || URL.contains("MAP")) return "wms";
		if(URL.contains("WFS")) return "wfs";
		
		return "crawler";
	}
	
	public void sendForServiceOrCrawler(String url, URL link) throws Exception{
		System.out.println("Identificando serviços");
		System.out.println("URL: " + url +"\n" + "Link" + link);
		if(this.identifyService(url).equals("wms-wfs")){
			this.getServicoWMS(link);
			this.setServicoWFS(link);
		}
		if(this.identifyService(url).equals("wms")){
			this.getServicoWMS(link);
		}else if (this.identifyService(url).equals("wfs")){
			this.setServicoWFS(link);
		}
	}
	
	public void linkCrawler(List<WebURL> urlss){
		
	Iterator i = urlss.iterator();
	
	while(i.hasNext()){
        	WebURL link = (WebURL) i.next();
        	if(link.getURL().contains("ows")||link.getURL().contains("MAPSERV")) {
        		possiveisServiço.add(link.getURL());
        		System.out.println("-----------URL SERVIÇO --------------");
        	}
    		
        	//System.out.println("Links da pagina: " + link.getURL());
        }
       if(!possiveisServiço.isEmpty()){
    	   try {
    		   System.out.println("Envia para o trata serviço");
				this.trataServiçoDoCrawler(possiveisServiço);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
	
	public void trataServiçoDoCrawler(List<String> servicos) throws Exception{
		System.out.println("Entrei n o serviço trataServiço");
		Iterator i = servicos.iterator();
		
		while(i.hasNext()){
			String servico = (String) i.next();
			String [] partUrl = servico.split("\\?");
			URL link = new URL(partUrl[0] + "?");
			this.sendForServiceOrCrawler(partUrl[0]+ "?", link);
		}
	}
	


	@Override
    public void visit(Page page) {          
            String url = page.getWebURL().getURL();
            System.out.println("URL: " + url);

            if (page.getParseData() instanceof HtmlParseData) {
                    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                    String html = htmlParseData.getHtml();
                    List<WebURL> links = htmlParseData.getOutgoingUrls();
                    
//                    System.out.println("Titulo da página:" + htmlParseData.getTitle());
//                    System.out.println("Tamanho do texto: " + htmlParseData.getText().length());
//                   // System.out.println("Texto: " + htmlParseData.getText());
//                    System.out.println("Tamanho do HTML: " + html.length());
//                    System.out.println("Numero de links: " + links.size());
                    
                    linkCrawler(links);
                   
                    
            }
    }
	
	public void getLayersWms(Iterator<Layer> layers) {
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
		System.out.println("to aqui");
		WebMapServer WMS = new WebMapServer(url);
		WMSCapabilities capabilitiesWMS = WMS.getCapabilities();
		List<Layer> layer = capabilitiesWMS.getLayerList();

		System.out.println("Quantidades de links de camadas:" + layer.size());
		Iterator<Layer> l = layer.iterator();
		this.getLayersWms(l);

		System.out.println();

	}
	
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
