package com.rayram23.webcrawler;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.rayram23.webcrawler.domain.Page;
import com.rayram23.webcrawler.fetch.PageFetcherPool;
import com.rayram23.webcrawler.parser.UriParser;
import com.rayram23.webcrawler.sitemap.SiteMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws URISyntaxException, MalformedURLException, InterruptedException
    {
       
    	String url = args[0];
    	int concurrentConnections = Integer.parseInt(args[1]);
    	int fetchTime = Integer.parseInt(args[2]);
    	
    	
    	URI startingUri = new URI(url);
    	
    	
    	
    	PageFetcherPool pool = new PageFetcherPool(concurrentConnections, fetchTime);
    	UriParser parser = new UriParser();
    	SiteMap siteMap = new SiteMap();
    	
    	
    	WebCrawler crawler = new WebCrawler(startingUri,siteMap,parser,pool);
    	pool.addPageFetchListener(crawler);
    	
    	crawler.start();
    	
    	while(true){
    		Thread.sleep((fetchTime + 5) * 1000);
    		if(pool.getActiveCount() > 0 || pool.getQueue().size() > 0){
    			continue;
    		}
    		Thread.sleep((fetchTime + 5) * 1000);
    		if(pool.getActiveCount() > 0 || pool.getQueue().size() > 0){
    			continue;
    		}
    		break;
    	}
    	siteMap.printMap();
    	System.exit(0);
    	
    	
    	
    }
}
