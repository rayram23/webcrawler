package com.rayram23.webcrawler.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Page {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Page other = (Page) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	private String url;
	private Set<String> images;
	private Set<String> links;
	private Set<String> statics;
	public Page(String url){
		this.url = url;
		this.images = new HashSet<String>();
		this.links =  new HashSet<String>();
		this.statics = new HashSet<String>();
	}
	public Set<String> getLinks(){
		return Collections.unmodifiableSet(this.links);
	}
	public Set<String> getStatics(){
		return Collections.unmodifiableSet(this.statics);
	}
	public Set<String> getImages(){
		return Collections.unmodifiableSet(this.images);
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
