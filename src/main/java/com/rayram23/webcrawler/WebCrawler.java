package com.rayram23.webcrawler;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rayram23.webcrawler.domain.Page;
import com.rayram23.webcrawler.fetch.PageFetchListener;
import com.rayram23.webcrawler.fetch.PageFetcherPool;
import com.rayram23.webcrawler.parser.UriParser;
import com.rayram23.webcrawler.sitemap.SiteMap;



public class WebCrawler implements PageFetchListener{

	
	private SiteMap siteMap;
	private URI uri;
	private Logger logger = Logger.getLogger("WebCrawler");
	private UriParser uriParser;
	private PageFetcherPool pool;
	private Set<String> seen;
	

	public WebCrawler(URI uri,  SiteMap siteMap, UriParser uriParser, PageFetcherPool pool){
		if(siteMap == null){
			throw new IllegalArgumentException("Please pass a valid SiteMap object.");
		}
		this.siteMap = siteMap;
		this.uriParser = uriParser;
		this.pool = pool;
		this.uri = uri;
		this.seen = new HashSet<String>();
	}
	public void start() throws MalformedURLException{
		String startLink = this.uri.toURL().toString();
		this.seen.add(startLink);
		this.pool.fetchPage(new Page(startLink));
	}
	public void pageFetched(Page page, Set<String> links, Set<String> images, Set<String> statics) {	
		//add the page to the graph
		logger.log(Level.INFO, "Page crawled: "+page.getUrl());
		page.addImages(images);
		page.addLinks(links);
		page.addStatics(statics);	
		this.siteMap.addPage(page);
		for(String link : links){
			//if the link is "seen" then some other page had a link to it. 
			//but it may have not been explotred yet we dont need to add
			//this link to the queue since it may already be there...
			//possible race condition (if one thead is adding the link while to other is checking
			//that the link exists in the seen set)
			boolean seen = this.seen.contains(link);
			if(seen){
				continue;
			}
			URI other = this.convertToURI(link);
			if(other == null){
				//logger.log(Level.WARNING, "Link is not a URI: "+link);
				continue;
			}
			boolean onDomain = this.uriParser.isUrlOnDomain(this.uri, other);
			if(!onDomain){
				//logger.log(Level.INFO, "Link is not on domain: "+link);
				continue;
			}
			this.seen.add(link);
			this.pool.fetchPage(new Page(link));
		}
	}
	protected URI convertToURI(String link){
		URI out = null;
		try{
			out = new URI(link);
		}
		catch(URISyntaxException e){
			
		}
		return out;
	}
}
