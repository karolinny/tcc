package br.edu.ifpb.crawler;

import java.io.File;
import java.util.List;

import br.edu.ifpb.servicos.TesteComOCrawler;
import br.edu.ifpb.servicos.VerifyService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;


public class Controller {
	private String crawlStorageFolder;//="/home/karol/crawler";
    private int NUMBER_OF_CRAWLER;
    private int MAX_DEPTH_OF_CRAWLING;
    private SettingsFile setting;
    private VerifyService verifyService;
    
    public Controller(){
    	 //setting = new SettingsFile(new File("br/edu/ifpb/crawler/Setting.xml"));
    	 initVariables();
    }
    public void initVariables(){
    	setting = new SettingsFile(new File("br/edu/ifpb/crawler/Setting.xml"));
    	this.crawlStorageFolder =  "/home/karol/crawler";
    	this.NUMBER_OF_CRAWLER = 7 ;//setting.settingsXML("NUMBER_OF_CRAWLER");
    	this.MAX_DEPTH_OF_CRAWLING =1; // setting.settingsXML("MAX_DEPTH_OF_CRAWLING");
    }

    public void callOfCrawler(String url) throws Exception{

	    CrawlConfig config = new CrawlConfig();
	    config.setMaxDepthOfCrawling(this.MAX_DEPTH_OF_CRAWLING);
	    config.setCrawlStorageFolder(crawlStorageFolder);
	
	    //Inicializa o controller para esse crawl
	    PageFetcher pageFetcher = new PageFetcher(config);
	    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
	    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
	    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
	    System.out.println("URL: " + url);
	    controller.addSeed(url+"/regional/web/");
	   // controller.addSeed(url+"/work/");
	    //controller.addSeed(url+"/portal/");
	    controller.addSeed(url);
	   // controller.addSeed(url+"/work/");
	   // controller.addSeed(url+"/portal/");
	    
	    controller.start(TesteComOCrawler.class, this.NUMBER_OF_CRAWLER); 
	    
	    System.out.println("Sai do Crawler");
	    
	   
	    
    }
}