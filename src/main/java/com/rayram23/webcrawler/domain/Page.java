package com.rayram23.webcrawler.domain;

public class Page {

	private Page parent;
	private String url;
	public Page(Page parent, String url){
		this.parent = parent;	
		this.url = url;
	}
	public Page getParent(){
		return this.parent;
	}
	public String getUrl(){
		return this.url;
	}
	
}
