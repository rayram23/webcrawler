package com.rayram23.webcrawler.fetch;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.rayram23.webcrawler.domain.Page;

public class PageFetcher implements Runnable {

	private Page page;
	private Set<PageFetchListener> listeners;
	public PageFetcher(Page page, Set<PageFetchListener> listeners){
		this.page = page;
		this.listeners = listeners;
	}
	public void run() {
		Document doc = null;
		try {
			doc = Jsoup.connect(page.getUrl()).get();
		} catch (IOException e) {
			
		}
		if(doc == null){
			return;
		}
		 Elements links = doc.select("a[href]");
	     Elements images = doc.select("img[src]");
	     Elements statics = doc.select("link[href]");
		
	     Set<String> fqdnLinks = this.getAllAbsoluteUrlForAttribute(links, "href");
	     Set<String> fqdnImages = this.getAllAbsoluteUrlForAttribute(images, "href");
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
