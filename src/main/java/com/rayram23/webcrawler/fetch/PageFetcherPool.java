package com.rayram23.webcrawler.fetch;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rayram23.webcrawler.WebCrawler;
import com.rayram23.webcrawler.domain.Page;

public class PageFetcherPool extends ScheduledThreadPoolExecutor{
	
	public static int MIN_DELAY_TIME = 1;

	private ScheduledExecutorService pool;
	private int delay;
	private Set<PageFetchListener> listeners;
	private Logger logger = Logger.getLogger("FetcherPool");
	
	
	public PageFetcherPool(int concurrentConnections, int delay){
		super(concurrentConnections);
		if(delay < PageFetcherPool.MIN_DELAY_TIME){
			throw new IllegalArgumentException("delay must be >= PageFetcherPool.MIN_DELAY_TIME");
		}
		this.delay = delay;
		this.listeners = new HashSet<PageFetchListener>();
	}
	public void fetchPage(Page page){
		
		PageFetcher fetcher = new PageFetcher(page,this.listeners);
		super.schedule(fetcher, this.delay, TimeUnit.SECONDS);
		//logger.log(Level.INFO, "Page scheduled to be crawled: "+page.getUrl());
		
	}
	public void addPageFetchListener(PageFetchListener listen){
		this.listeners.add(listen);
	}
	public int getActiveCount(){
		return super.getActiveCount();
	}
}
