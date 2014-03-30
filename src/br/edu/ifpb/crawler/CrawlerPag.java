package br.edu.ifpb.crawler;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class CrawlerPag extends WebCrawler {

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
                        
                        
                        Iterator i = links.iterator();
                        
                        while(i.hasNext()){
                        	WebURL link = (WebURL) i.next();
                        	System.out.println("Links da pagina: " + link.getURL());
                        }
                        
                        System.out.println();
                }
        }
}