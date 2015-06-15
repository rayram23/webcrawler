package com.rayram23.webcrawler.fetch;

import static org.junit.Assert.*;

import org.junit.Test;

public class PageFetchPoolTest {

	@Test
	public void testConstructorDelayLessThanAllowedValue() {
		
		int delay = PageFetcherPool.MIN_DELAY_TIME - 1;
		boolean exceptionThrown = false;
		
		try{
			new PageFetcherPool(10,delay);
		}
		catch(IllegalArgumentException e){
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
	}
	@Test
	public void testConstructorDelayEqualToAllowedValues(){
		int delay = PageFetcherPool.MIN_DELAY_TIME;
		boolean exceptionThrown = false;
		
		try{
			new PageFetcherPool(10,delay);
		}
		catch(IllegalArgumentException e){
			exceptionThrown = true;
		}
		assertFalse(exceptionThrown);
	}
	@Test
	public void testConstructorDelayGreaterTahnAllowedValues(){
		int delay = PageFetcherPool.MIN_DELAY_TIME + 1;
		boolean exceptionThrown = false;
		
		try{
			new PageFetcherPool(10,delay);
		}
		catch(IllegalArgumentException e){
			exceptionThrown = true;
		}
		assertFalse(exceptionThrown);
	}

}
