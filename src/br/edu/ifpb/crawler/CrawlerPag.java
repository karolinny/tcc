package br.edu.ifpb.crawler;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.geotools.ows.ServiceException;

import br.edu.ifpb.servicos.VerifyService;
import br.edu.ifpb.servicos.WebMapServerLayers;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class CrawlerPag extends WebCrawler {
	private VerifyService verifyService;
	private WebMapServerLayers wmsLayers;
	private List<String> possiveisServiço = new ArrayList<String>();
	List<WebURL> links = new ArrayList<>();

        private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
                                                          + "|png|tiff?|mid|mp2|mp3|mp4"
                                                          + "|wav|avi|mov|mpeg|ram|m4v|pdf" 
                                                          + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

        //	Função implementada para verificar se a URL vai ser rastreada ou não
        @Override
        public boolean shouldVisit(WebURL url) {
                String href = url.getURL().toLowerCase();
                return !FILTERS.matcher(href).matches() && href.startsWith("http://www.uol.com.br/");
        }

        // Chamada quando a pagina esta carrega e pronta para ser processada
        @Override
        public void visit(Page page) {          
                String url = page.getWebURL().getURL();
                System.out.println("URL: " + url);

                if (page.getParseData() instanceof HtmlParseData) {
                        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                        String html = htmlParseData.getHtml();
                        List<WebURL> links = htmlParseData.getOutgoingUrls();
                        
                        System.out.println("Titulo da página:" + htmlParseData.getTitle());
                        System.out.println("Tamanho do texto: " + htmlParseData.getText().length());
                       // System.out.println("Texto: " + htmlParseData.getText());
                        System.out.println("Tamanho do HTML: " + html.length());
                        System.out.println("Numero de links: " + links.size());
                        
                    teste(links);
                       
                        
                }
        }
        
        public void teste(List<WebURL>l){
        	System.out.println( "tests" +       	l.size());
        }
        
     
}