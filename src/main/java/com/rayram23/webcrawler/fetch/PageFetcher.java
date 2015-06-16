package com.rayram23.webcrawler.fetch;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.rayram23.webcrawler.domain.Page;

public class PageFetcher implements Runnable {

	private Page page;
	private Set<PageFetchListener> listeners;
	private Logger logger = Logger.getLogger("fetcher");
	public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36";
	public PageFetcher(Page page, Set<PageFetchListener> listeners){
		this.page = page;
		this.listeners = listeners;
	}
	public void run() {
		//logger.log(Level.INFO, "Attempting to crawl: "+this.page.getUrl());
		
		//congiure a connection
		Connection connection = Jsoup.connect(page.getUrl());
		//added this to get sites that bloack coded crawlers to work.. 
		connection.userAgent(PageFetcher.USER_AGENT);
		Document doc = null;
		try {
			doc = connection.get();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		if(doc == null){
			return;
		}
		 Elements links = doc.select("a[href]");
	     Elements images = doc.select("img[src]");
	     Elements statics = doc.select("link[href]");
		
	     Set<String> fqdnLinks = this.getAllAbsoluteUrlForAttribute(links, "href");
	     Set<String> fqdnImages = this.getAllAbsoluteUrlForAttribute(images, "src");
	     Set<String> fqdnStatics = this.getAllAbsoluteUrlForAttribute(statics, "href");
	     
	     for(PageFetchListener listener : this.listeners){
	    	 listener.pageFetched(page, fqdnLinks, fqdnImages, fqdnStatics);
	     }
	     

	}
	
	protected Set<String> getAllAbsoluteUrlForAttribute(Elements elements, String attribute){
		Set<String> urls = new HashSet<String>();
		if(elements == null || attribute == null){
			return urls;
		}
		for(Element link : elements){
			String absUrl = link.absUrl(attribute);
			if(absUrl == null || absUrl.isEmpty()){
				continue;
			}
			urls.add(absUrl);
		}
		return urls;
	}
	

}
