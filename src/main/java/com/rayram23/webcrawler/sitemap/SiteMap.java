package com.rayram23.webcrawler.sitemap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rayram23.webcrawler.domain.Page;

public class SiteMap {

	private Map<String,Page> site = new HashMap<String,Page>();
	private Logger logger = Logger.getLogger("SiteMap");
	private ReentrantLock lock = new ReentrantLock();
	
	
	
	public void addPage(Page newPage){
		this.lock.lock();
		this.site.put(newPage.getUrl(), newPage);
		this.lock.unlock();
	}
	public void printMap(){
		for(Entry<String,Page> entry : this.site.entrySet()){
			System.out.println( "Page: "+entry.getKey());
			System.out.println("Links: ");
			this.printValues(entry.getValue().getLinks(), "\t");
			System.out.println( "Images: ");
			this.printValues(entry.getValue().getImages(), "\t");
			System.out.println( "Statics: ");
			this.printValues(entry.getValue().getStatics(), "\t");
		}
	}
	protected void printValues(Set<String> vals, String indent){
		for(String s : vals){
			System.out.println( indent+s);
		}
	}
	public boolean exists(String url){
		return this.site.containsKey(url);
	}
	
}
