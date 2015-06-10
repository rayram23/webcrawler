package com.rayram23.webcrawler.fetch;

import java.util.Set;

import com.rayram23.webcrawler.domain.Page;

public interface PageFetchListener {

	public void pageFetched(Page page, Set<String> links);
}
