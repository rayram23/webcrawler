package com.rayram23.webcrawler.fetch;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.text.html.HTML.Tag;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.rayram23.webcrawler.domain.Page;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Jsoup.class)
public class PageFetcherTest {

	
	@Test
	public void testRunIoException() throws IOException{
		Page page = Mockito.mock(Page.class);
		boolean exceptionThrown = false;
		Connection mockedConnection = Mockito.mock(Connection.class);
		
		PowerMockito.mockStatic(Jsoup.class);
		PowerMockito.when(Jsoup.connect(Mockito.anyString())).thenReturn(mockedConnection);
		Mockito.when(mockedConnection.get()).thenThrow(new IOException("UNIT TESTING"));
		Mockito.when(page.getUrl()).thenReturn("someurlthatdoesntmatterforthistest");
		
		PageFetcher classUnderTest = new PageFetcher(page,null);
		
		try{
			classUnderTest.run();
		}
		catch(Exception e){
			exceptionThrown = true;
		}
		assertFalse(exceptionThrown);
		
		
		
	}
	@Test
	public void testGetAllAbsoluteUrlForAttributeNullElements(){
		Page page = Mockito.mock(Page.class);
		PageFetcher classUnderTest = new PageFetcher(page,null);
		
		Set<String> results = classUnderTest.getAllAbsoluteUrlForAttribute(null, "somettar");
		assertNotNull(results);
		assertTrue(results.isEmpty());	
	}
	@Test
	public void testGetAllAbsoluteUrlsForAttributeNullAttribute(){
		Page page = Mockito.mock(Page.class);
		PageFetcher classUnderTest = new PageFetcher(page,null);
		Elements elements = new Elements();
		
		
		Set<String> results = classUnderTest.getAllAbsoluteUrlForAttribute(elements,null);
		
		
		assertNotNull(results);
		assertTrue(results.isEmpty());	
	}
	@Test
	public void testGetAllAbsoluteUrlsForAttributeAttribute(){
		
		Page page = Mockito.mock(Page.class);
		PageFetcher classUnderTest = new PageFetcher(page,null);
		Elements elements = new Elements();
		Element e1 = Mockito.mock(Element.class);
		Element e2 = Mockito.mock(Element.class);
		elements.add(e1);
		elements.add(e2);
		
		String testLink = "testLink";
		
		Mockito.when(e1.absUrl("href")).thenReturn(testLink);
		Mockito.when(e2.absUrl("href")).thenReturn("");
		
		Set<String> results = classUnderTest.getAllAbsoluteUrlForAttribute(elements,"href");
		
		
		assertNotNull(results);
		assertFalse(results.isEmpty());	
		assertTrue(results.size() == 1);
		assertTrue(results.contains(testLink));
		
	}
	@Test
	public void testRunNotifyListeners() throws IOException{
		Page page = Mockito.mock(Page.class);
		PageFetchListener listener = Mockito.mock(PageFetchListener.class);
		Set<PageFetchListener> listeners = new HashSet<PageFetchListener>();
		listeners.add(listener);
		PageFetcher classUnderTest = Mockito.spy(new PageFetcher(page,listeners));
		PowerMockito.mockStatic(Jsoup.class);
		Connection connection  = Mockito.mock(Connection.class);
		Document doc = Mockito.mock(Document.class);
		Elements elements1 = new Elements();
		Elements elements2 = new Elements();
		Elements elements3 = new Elements();
		Element e1 = Mockito.mock(Element.class);
		Element e2 = Mockito.mock(Element.class);
		Element e3 = Mockito.mock(Element.class);
		elements1.add(e1);
		elements2.add(e2);
		elements3.add(e3);
		Set<String> links = new HashSet<String>();
		Set<String> statics = new HashSet<String>();
		Set<String> images = new HashSet<String>();
		
		
		PowerMockito.when(Jsoup.connect(Mockito.anyString())).thenReturn(connection);
		Mockito.when(connection.get()).thenReturn(doc);
		Mockito.doReturn(elements1).when(doc).select("a[href]");
		Mockito.doReturn(elements2).when(doc).select("img[src");
		Mockito.doReturn(elements3).when(doc).select("link[href]");
		Mockito.doReturn(links).when(classUnderTest).getAllAbsoluteUrlForAttribute(elements3, "href");
		Mockito.doReturn(images).when(classUnderTest).getAllAbsoluteUrlForAttribute(elements2, "href");
		Mockito.doReturn(statics).when(classUnderTest).getAllAbsoluteUrlForAttribute(elements1, "href");
		
		classUnderTest.run();
		
		Mockito.verify(listener,Mockito.times(1)).pageFetched(page, links, images, statics);

	}


}
