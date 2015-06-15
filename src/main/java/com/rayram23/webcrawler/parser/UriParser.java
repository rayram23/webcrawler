package com.rayram23.webcrawler.parser;

import java.net.URI;

public class UriParser {

	public boolean isUrlOnDomain( URI check,URI parentDomain){
		String host = this.extractDomain(check);
		String parentHost = this.extractDomain(parentDomain);
		
		return parentHost.toLowerCase().equals(host.toLowerCase());
	}
	public String extractDomain(URI uri){
		  String domain = uri.getHost().toLowerCase();
		  //check if there is a leading www
		  if(domain.startsWith("www.")){
			   domain =  domain.substring(4);
		   }
		 return domain;
	}
}
