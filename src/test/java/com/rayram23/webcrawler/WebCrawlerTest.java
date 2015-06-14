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

import com.rayram23.webcrawler.WebCrawler;
import com.rayram23.webcrawler.domain.Page;
import com.rayram23.webcrawler.parser.UriParser;
import com.rayram23.webcrawler.sitemap.SiteMap;

public class WebCrawlerTest {

	@Test
	public void testIllegalArgumentFetchTimeGreaterThanLimit() throws URISyntaxException{
		
		Random r = new Random();
	
		int valueUnderTest = WebCrawler.MIN_FETCH_TIME + Math.abs( r.nextInt() );
		boolean illegalArgumentExceptionThrown = false;
		URI mockedURL = new URI("http://rayram.com");
		SiteMap mockedSiteMap = Mockito.mock(SiteMap.class);
		ExecutorService mockExecutorService = Mockito.mock(ExecutorService.class);
		Queue<Page> fakeQueue = Mockito.mock(Queue.class);
		UriParser parser = Mockito.mock(UriParser.class);
		try{
			new WebCrawler(mockedURL,Boolean.TRUE,valueUnderTest, Boolean.TRUE,mockedSiteMap,fakeQueue, mockExecutorService,parser);
		}
		catch(IllegalArgumentException e){
			illegalArgumentExceptionThrown = true;
		}
		assertFalse(illegalArgumentExceptionThrown);
		
	}
	@Test
	public void testIllegalArgumentFetchTimeEqualToLimit() throws URISyntaxException {
		Random r = new Random();
		
		int valueUnderTest = WebCrawler.MIN_FETCH_TIME;
		boolean illegalArgumentExceptionThrown = false;
		URI mockedURL = new URI("http://rayram.com");
		SiteMap mockedSiteMap = Mockito.mock(SiteMap.class);
		ExecutorService mockExecutorService = Mockito.mock(ExecutorService.class);
		Queue<Page> fakeQueue = Mockito.mock(Queue.class);
		UriParser parser = Mockito.mock(UriParser.class);

		try{
			new WebCrawler(mockedURL,Boolean.TRUE,valueUnderTest, Boolean.TRUE,mockedSiteMap,fakeQueue, mockExecutorService,parser);
		}
		catch(IllegalArgumentException e){
			illegalArgumentExceptionThrown = true;
		}
		assertFalse(illegalArgumentExceptionThrown);
	}
	@Test
	public void testIllegalArgumentFetchTimeLessThanLimit() throws URISyntaxException {
		Random r = new Random();
		
		int valueUnderTest = WebCrawler.MIN_FETCH_TIME - Math.abs(r.nextInt());
		boolean illegalArgumentExceptionThrown = false;
		URI mockedURL = new URI("http://rayram.com");
		SiteMap mockedSiteMap = Mockito.mock(SiteMap.class);
		ExecutorService mockExecutorService = Mockito.mock(ExecutorService.class);
		Queue<Page> fakeQueue = Mockito.mock(Queue.class);
		UriParser parser = Mockito.mock(UriParser.class);

		try{
			new WebCrawler(mockedURL,Boolean.TRUE,valueUnderTest, Boolean.TRUE,mockedSiteMap,fakeQueue, mockExecutorService,parser);
		}
		catch(IllegalArgumentException e){
			illegalArgumentExceptionThrown = true;
		}
		assertTrue(illegalArgumentExceptionThrown);
	}
	@Test
	public void testPageFetchedPageAddedToSiteMap() throws URISyntaxException {
		int fetchTime = WebCrawler.MIN_FETCH_TIME;
		URI mockedURL = new URI("http://rayram.com");
		SiteMap mockedSiteMap = Mockito.mock(SiteMap.class);
		ExecutorService mockExecutorService = Mockito.mock(ExecutorService.class);
		Queue<Page> fakeQueue = Mockito.mock(Queue.class);
		Page fakePage = Mockito.mock(Page.class);
		Page parentPage = Mockito.mock(Page.class);
		Set<String> links = new HashSet<String>();
		UriParser parser = Mockito.mock(UriParser.class);

		
		Mockito.when(fakePage.getParent()).thenReturn(parentPage);
		
		WebCrawler testClass = new WebCrawler(mockedURL,Boolean.TRUE, fetchTime,Boolean.TRUE,mockedSiteMap,fakeQueue,mockExecutorService,parser);
		testClass.pageFetched(fakePage, links,Collections.EMPTY_SET,Collections.EMPTY_SET);
		
		
		Mockito.verify(mockedSiteMap, Mockito.times(1)).addPage(parentPage, fakePage);
	}
	@Test
	public void testPageFetchedAllLinksAddedToQueue() throws URISyntaxException {
		int fetchTime = WebCrawler.MIN_FETCH_TIME;
		URI mockedURL = new URI("http://rayram.com");
		SiteMap mockedSiteMap = Mockito.mock(SiteMap.class);
		ExecutorService mockExecutorService = Mockito.mock(ExecutorService.class);
		Queue<Page> fakeQueue = Mockito.mock(Queue.class);
		Page fakePage = Mockito.mock(Page.class);
		Page parentPage = Mockito.mock(Page.class);
		UriParser parser = Mockito.mock(UriParser.class);

		
		Set<String> links = new HashSet<String>();
		String site1 = "Site1";
		String site2 = "Site2";
		String site3 = "Site3";
		links.add(site1);
		links.add(site2);
		links.add(site3);
		ArgumentCaptor<Page> pageCapture = ArgumentCaptor.forClass(Page.class);

		Mockito.when(fakePage.getParent()).thenReturn(parentPage);
		Mockito.when(parser.isUrlOnDomain(Mockito.any(URI.class), Mockito.anyString())).thenReturn(true);
		WebCrawler testClass = new WebCrawler(mockedURL,Boolean.TRUE, fetchTime,Boolean.TRUE,mockedSiteMap,fakeQueue,mockExecutorService,parser);
		testClass.pageFetched(fakePage, links,Collections.EMPTY_SET,Collections.EMPTY_SET);
		
		Mockito.verify(fakeQueue,Mockito.times(links.size())).add(pageCapture.capture());
		assertEquals(links.size(),pageCapture.getAllValues().size());
		for(Page p : pageCapture.getAllValues()){
			assertTrue(links.contains(p.getUrl()));
		}
		
	}
	@Test
	public void testPageFetchedNoLinksAddedToQueue() throws URISyntaxException{
		int fetchTime = WebCrawler.MIN_FETCH_TIME;
		URI mockedURL = new URI("http://rayram.com");
		SiteMap mockedSiteMap = Mockito.mock(SiteMap.class);
		ExecutorService mockExecutorService = Mockito.mock(ExecutorService.class);
		Queue<Page> fakeQueue = Mockito.mock(Queue.class);
		Page fakePage = Mockito.mock(Page.class);
		Page parentPage = Mockito.mock(Page.class);
		UriParser parser = Mockito.mock(UriParser.class);

		
		Set<String> links = new HashSet<String>();
		String site1 = "Site1";
		String site2 = "Site2";
		String site3 = "Site3";
		links.add(site1);
		links.add(site2);
		links.add(site3);
		ArgumentCaptor<Page> pageCapture = ArgumentCaptor.forClass(Page.class);

		Mockito.when(fakePage.getParent()).thenReturn(parentPage);
		Mockito.when(parser.isUrlOnDomain(Mockito.any(URI.class), Mockito.anyString())).thenReturn(false);
		WebCrawler testClass = new WebCrawler(mockedURL,Boolean.TRUE, fetchTime,Boolean.TRUE,mockedSiteMap,fakeQueue,mockExecutorService,parser);
		testClass.pageFetched(fakePage, links,Collections.EMPTY_SET,Collections.EMPTY_SET);
		
		Mockito.verify(fakeQueue,Mockito.times(0)).add(pageCapture.capture());
		
	}
}
