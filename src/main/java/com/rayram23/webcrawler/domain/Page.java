package com.rayram23.webcrawler.domain;

import java.util.HashSet;
import java.util.Set;

public class Page {

	private Page parent;
	private String url;
	private Set<String> images;
	private Set<String> links;
	private Set<String> statics;
	public Page(Page parent, String url){
		this.parent = parent;	
		this.url = url;
		this.images = new HashSet<String>();
		this.links =  new HashSet<String>();
		this.statics = new HashSet<String>();
	}
	public Page getParent(){
		return this.parent;
	}
	public String getUrl(){
		return this.url;
	}
	public void addImage(String url){
		this.images.add(url);
	}
	public void addImages(Set<String> urls){
		this.images.addAll(urls);
	}
	public void addLinks(Set<String> urls){
		this.links.addAll(urls);
	}
	public void addLink(String url){
		this.links.add(url);
	}
	public void addStatics(Set<String> urls){
		this.statics.addAll(urls);
	}
	
}
