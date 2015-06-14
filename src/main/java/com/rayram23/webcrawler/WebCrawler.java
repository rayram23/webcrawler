package com.rayram23.webcrawler;

import java.net.URI;
import java.net.URL;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import com.rayram23.webcrawler.domain.Page;
import com.rayram23.webcrawler.fetch.PageFetchListener;
import com.rayram23.webcrawler.parser.UriParser;
import com.rayram23.webcrawler.sitemap.SiteMap;



public class WebCrawler implements PageFetchListener{

public static final int MIN_FETCH_TIME = 1000;
	
	private SiteMap siteMap;
	private Boolean allowSubdomains;
	private URI url;
	private Boolean obeyRobots;
	private Logger logger = Logger.getLogger("WebCrawler");
	private Queue<Page> pageQueue;
	private ExecutorService executor;
	private UriParser uriParser;
	

	public WebCrawler(URI uri, Boolean allowSubdomains, int fetchTime, Boolean obeyRobots, SiteMap siteMap, Queue<Page> queue, ExecutorService executor,UriParser uriParser){
		if(fetchTime < WebCrawler.MIN_FETCH_TIME){
			throw new IllegalArgumentException("fetchTime must be >= WebCrawler.MIN_FETCH_TIME");
		}
		if(siteMap == null){
			throw new IllegalArgumentException("Please pass a valid SiteMap object.");
		}
		this.pageQueue = queue;
		this.executor = executor;
		this.siteMap = siteMap;
		this.uriParser = uriParser;
	}
	public void pageFetched(Page page, Set<String> links, Set<String> images, Set<String> statics) {	
		//add the page to the graph
		this.siteMap.addPage(page.getParent(), page);
		//for each link
		for(String link : links){
			//if the page is on the original domain add it to the queue
			if(this.uriParser.isUrlOnDomain(this.url, link)){
				this.pageQueue.add(new Page(page,link));
			}
		}
	}
}
