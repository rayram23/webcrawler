package com.rayram23.webcrawler.parser;

import java.net.URI;

public class UriParser {

	public boolean isUrlOnDomain(String parentDomain, String check){
		return false;
	}
	public String extractDomain(URI uri){
		  String domain = uri.getHost().toLowerCase();
		  //check if there is a leading www
		  if(domain.startsWith("www.")){
			   domain =  domain.substring(4);
		   }
		   //at this point 
		  return null;
	}
}
