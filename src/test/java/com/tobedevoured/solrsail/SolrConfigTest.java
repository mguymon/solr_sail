package com.tobedevoured.solrsail;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.tobedevoured.solrsail.SolrConfig;

public class SolrConfigTest {

	private SolrConfig helper;
	private File solrHome;
	
	@Before
	public void setup() throws IOException {
		helper = new SolrConfig();
		
		solrHome = new File( helper.getSolrHome() );
		
		if ( solrHome.exists() ) {
			FileUtils.forceDelete( solrHome );
		}
	}
	
	@Test
	public void getSolrHome() {
		assertEquals( "target/test-solr", helper.getSolrHome() );
	}
	
	
	@Test
	public void installFromClasspath() throws IOException {
		helper.installFromClasspath();
		
		assertTrue( (new File( helper.getSolrHome() + File.separator + "solr.xml")).exists() );
	}
	
	@Test
	public void install() throws IOException, URISyntaxException {
		helper.install();
		
		assertTrue( (new File( helper.getSolrHome() + File.separator + "solr.xml")).exists() );
	}
}
