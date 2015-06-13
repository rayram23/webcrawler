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
	public PageFetcher(Page page){
		this.page = page;
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
	     Elements media = doc.select("[src]");
	     Elements imports = doc.select("link[href]");
		
	     
	     

	}
	protected Set<String> getAllImages(Elements media){
		Set<String> urls = new HashSet<String>();
		for (Element src : media) {
            if (!src.tagName().equals("img")){
            	continue;
            }
            String fqdUrl = src.attr("abs:src");
            urls.add(fqdUrl);
        }
		return urls;
	}

}
