package com.rayram23.webcrawler.fetch;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Set;

import javax.swing.text.html.HTML.Tag;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
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
		
		PageFetcher classUnderTest = new PageFetcher(page);
		
		try{
			classUnderTest.run();
		}
		catch(Exception e){
			exceptionThrown = true;
		}
		assertFalse(exceptionThrown);
		
		
		
	}
	@Test
	public void testGetAllImagesNoImages(){
		Elements media = new Elements();
		Page page = Mockito.mock(Page.class);
		
		
		PageFetcher classUnderTest = new PageFetcher(page);
		Set<String> images = classUnderTest.getAllImages(media);
		
		assertNotNull(images);
		assertTrue(images.isEmpty());
	}
	@Test
	public void testgetAllImagesNoTags(){
		Elements media = new Elements();
		Page page = Mockito.mock(Page.class);
		Element element = Mockito.mock(Element.class);
		media.add(element);
		
		Mockito.when(element.tagName()).thenReturn("not-img");
		
		PageFetcher classUnderTest = new PageFetcher(page);
		Set<String> images = classUnderTest.getAllImages(media);
		
		assertNotNull(images);
		assertTrue(images.isEmpty());
	}
	@Test
	public void testgetAllImagesWithTags(){
		Elements media = new Elements();
		Page page = Mockito.mock(Page.class);
		Element element = Mockito.mock(Element.class);
		media.add(element);
		
		Mockito.when(element.tagName()).thenReturn("img");
		
		PageFetcher classUnderTest = new PageFetcher(page);
		Set<String> images = classUnderTest.getAllImages(media);
		
		assertNotNull(images);
		assertFalse(images.isEmpty());
	}

}
