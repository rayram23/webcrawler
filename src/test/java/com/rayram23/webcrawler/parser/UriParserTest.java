package com.rayram23.webcrawler.parser;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class UriParserTest {

	
	@Test
	public void testExtractDomainSimple() throws URISyntaxException{
		URI testUri = new URI("http://rayram.com");
		UriParser testClass = new UriParser();
		assertEquals("rayram.com",testClass.extractDomain(testUri));
	}
	@Test
	public void testExtractDomainWithSubDomain() throws URISyntaxException{
		URI testUri = new URI("http://subdomain.rayram.com");
		UriParser testClass = new UriParser();
		assertEquals("subdomain.rayram.com",testClass.extractDomain(testUri));
	}
	@Test
	public void testExtractDomainWithSubPage() throws URISyntaxException{
		URI testUri = new URI("http://rayram.com/page.html");
		UriParser testClass = new UriParser();
		assertEquals("rayram.com",testClass.extractDomain(testUri));
	}
	@Test
	public void testIsUrlOnDomainTrue() throws URISyntaxException{
		URI testUri = new URI("http://rayram.com/page.html");
		UriParser testClass = new UriParser();
		assertTrue("rayram.com",testClass.isUrlOnDomain(testUri, "rayram.com"));
	}
	@Test
	public void testIsUrlOnDomainFalse() throws URISyntaxException{
		URI testUri = new URI("http://rayram.googlr.com/page.html");
		UriParser testClass = new UriParser();
		assertFalse("rayram.com",testClass.isUrlOnDomain(testUri, "rayram.com"));
	}
}
