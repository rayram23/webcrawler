package com.rayram23.webcrawler;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import com.rayram23.webcrawler.WebCrawler;
import com.rayram23.webcrawler.domain.Page;
import com.rayram23.webcrawler.fetch.PageFetcherPool;
import com.rayram23.webcrawler.parser.UriParser;
import com.rayram23.webcrawler.sitemap.SiteMap;

public class WebCrawlerTest {

	
	@Test
	public void testPageFetchedPageAddedToSiteMap() throws URISyntaxException {
		URI mockedURL = new URI("http://rayram.com");
		SiteMap mockedSiteMap = Mockito.mock(SiteMap.class);
		Page fakePage = Mockito.mock(Page.class);
		Page parentPage = Mockito.mock(Page.class);
		Set<String> links = new HashSet<String>();
		UriParser parser = Mockito.mock(UriParser.class);
		
		
		
		WebCrawler testClass = new WebCrawler(mockedURL,mockedSiteMap,parser,Mockito.mock(PageFetcherPool.class));
		testClass.pageFetched(fakePage, links,Collections.EMPTY_SET,Collections.EMPTY_SET);
		
		
		Mockito.verify(mockedSiteMap, Mockito.times(1)).addPage(fakePage);
	}
	@Test
	public void testPageFetchedAllLinksAddedToQueue() throws URISyntaxException {
		URI mockedURL = new URI("http://rayram.com");
		SiteMap mockedSiteMap = Mockito.mock(SiteMap.class);
		Page fakePage = Mockito.mock(Page.class);
		Page parentPage = Mockito.mock(Page.class);
		UriParser parser = Mockito.mock(UriParser.class);
		PageFetcherPool pool = Mockito.mock(PageFetcherPool.class);
		
		Set<String> links = new HashSet<String>();
		String site1 = "Site1";
		String site2 = "Site2";
		String site3 = "Site3";
		links.add(site1);
		links.add(site2);
		links.add(site3);
		ArgumentCaptor<Page> pageCapture = ArgumentCaptor.forClass(Page.class);
		WebCrawler testClass = new WebCrawler(mockedURL,mockedSiteMap,parser, pool);
		
		Mockito.when(parser.isUrlOnDomain(Mockito.any(URI.class), Mockito.any(URI.class))).thenReturn(true);
		
		testClass.pageFetched(fakePage, links,Collections.EMPTY_SET,Collections.EMPTY_SET);
		
		Mockito.verify(pool,Mockito.times(links.size())).fetchPage((pageCapture.capture()));
		assertEquals(links.size(),pageCapture.getAllValues().size());
		for(Page p : pageCapture.getAllValues()){
			assertTrue(links.contains(p.getUrl()));
		}
		
	}
	@Test
	public void testPageFetchedNoLinksAddedToQueue() throws URISyntaxException{
		URI mockedURL = new URI("http://rayram.com");
		SiteMap mockedSiteMap = Mockito.mock(SiteMap.class);
		Page fakePage = Mockito.mock(Page.class);
		Page parentPage = Mockito.mock(Page.class);
		UriParser parser = Mockito.mock(UriParser.class);
		PageFetcherPool pool = Mockito.mock(PageFetcherPool.class);
		
		Set<String> links = new HashSet<String>();
		String site1 = "Site1";
		String site2 = "Site2";
		String site3 = "Site3";
		links.add(site1);
		links.add(site2);
		links.add(site3);
		ArgumentCaptor<Page> pageCapture = ArgumentCaptor.forClass(Page.class);

		Mockito.when(parser.isUrlOnDomain(Mockito.any(URI.class), Mockito.any(URI.class))).thenReturn(false);
		WebCrawler testClass = new WebCrawler(mockedURL,mockedSiteMap,parser,pool);
		testClass.pageFetched(fakePage, links,Collections.EMPTY_SET,Collections.EMPTY_SET);
		
		Mockito.verify(pool,Mockito.times(0)).fetchPage(pageCapture.capture());
		
	}
	@Test
	public void testPageFetchedLinksAlreadyExist() throws URISyntaxException{
		URI mockedURL = new URI("http://rayram.com");
		SiteMap mockedSiteMap = Mockito.mock(SiteMap.class);
		Page fakePage = Mockito.mock(Page.class);
		Page parentPage = Mockito.mock(Page.class);
		UriParser parser = Mockito.mock(UriParser.class);
		PageFetcherPool pool = Mockito.mock(PageFetcherPool.class);
		
		Set<String> links = new HashSet<String>();
		String site1 = "Site1";
		String site2 = "Site2";
		String site3 = "Site3";
		links.add(site1);
		links.add(site2);
		links.add(site3);
		ArgumentCaptor<Page> pageCapture = ArgumentCaptor.forClass(Page.class);

		Mockito.when(mockedSiteMap.exists(site1)).thenReturn(true);
		Mockito.when(mockedSiteMap.exists(site2)).thenReturn(true);
		Mockito.when(mockedSiteMap.exists(site3)).thenReturn(true);
		
		WebCrawler testClass = new WebCrawler(mockedURL,mockedSiteMap,parser,pool);
		testClass.pageFetched(fakePage, links,Collections.EMPTY_SET,Collections.EMPTY_SET);
		
		Mockito.verify(pool,Mockito.times(0)).fetchPage(pageCapture.capture());
	}
	@Test
	public void testConvertToURIBadURI() throws URISyntaxException{
		
		String uri = "http://localhost:8080/test file.txt";
		URI mockedURL = new URI("http://rayram.com");
		SiteMap mockedSiteMap = Mockito.mock(SiteMap.class);
		UriParser parser = Mockito.mock(UriParser.class);
		PageFetcherPool pool = Mockito.mock(PageFetcherPool.class);
		
		WebCrawler testClass = new WebCrawler(mockedURL,mockedSiteMap,parser,pool);
		
		URI val = testClass.convertToURI(uri);
		
		assertNull(val);
		
		
	}
	@Test
	public void testConvertToURIGoodURI() throws URISyntaxException{
		
		String uri = "http://gooduri.com";
		URI mockedURL = new URI("http://rayram.com");
		SiteMap mockedSiteMap = Mockito.mock(SiteMap.class);
		
		UriParser parser = Mockito.mock(UriParser.class);
		PageFetcherPool pool = Mockito.mock(PageFetcherPool.class);
		
		WebCrawler testClass = new WebCrawler(mockedURL,mockedSiteMap,parser,pool);
		
		URI val = testClass.convertToURI(uri);
		
		assertNotNull(val);
		
		
	}
	@Test
	public void testConstrucutorNullSiteMap() throws URISyntaxException{
		boolean exceptionThrown = false;
		try{
			WebCrawler crawler = new WebCrawler(new URI("http://google.com"), null, Mockito.mock(UriParser.class),Mockito.mock(PageFetcherPool.class));
		}
		catch(IllegalArgumentException e){
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}
	@Test
	public void testPageFetchedLinkAlreadySeen() throws URISyntaxException{
		
		Set<String> seen = new HashSet<String>();
		String link = "a seen url";
		seen.add(link);
		Set<String> images = new HashSet<String>();
		Set<String> statics = new HashSet<String>();
		Set<String> links = new HashSet<String>();
		links.add(link);
		PageFetcherPool pool = Mockito.mock(PageFetcherPool.class);
		SiteMap siteMap = Mockito.mock(SiteMap.class);
		UriParser parser = Mockito.mock(UriParser.class);
		Page page = Mockito.mock(Page.class);
		
		
		WebCrawler crawler = new WebCrawler(new URI("http://rayram.com"),siteMap, parser, pool);
		
		Whitebox.setInternalState(crawler, "seen", seen);
		
		crawler.pageFetched(page, links, images, statics);
		
		Mockito.verify(pool,Mockito.times(0)).fetchPage(Mockito.any(Page.class));
		
		
	}
}
