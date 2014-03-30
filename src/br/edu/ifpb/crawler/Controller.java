package br.edu.ifpb.crawler;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


public class Controller {
	private String crawlStorageFolder = "/home/karol/crawler";
    private static int NUMBER_OF_CRAWLER= 8;
    private static int MAX_DEPTH_OF_CRAWLING= 7;

    public void callOfCrawler(String url) throws Exception{

	    CrawlConfig config = new CrawlConfig();
	    config.setMaxDepthOfCrawling(Controller.MAX_DEPTH_OF_CRAWLING);
	    config.setCrawlStorageFolder(crawlStorageFolder);
	
	    //Inicializa o controller para esse crawl
	    PageFetcher pageFetcher = new PageFetcher(config);
	    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
	    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
	    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
	    System.out.println("URL: " + url);
	    controller.addSeed(url);
	    controller.addSeed(url+"/work/");
	    controller.addSeed(url+"/portal/");
	    controller.start(CrawlerPag.class, Controller.NUMBER_OF_CRAWLER); 
    }
}