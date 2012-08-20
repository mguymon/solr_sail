package com.slackworks.solrsail;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.modeshape.common.util.FileUtil;

public class SolrTest {

	private Solr helper;
	private File solrHome;
	
	@Before
	public void setup() throws IOException {
		helper = new Solr();
		
		solrHome = new File( helper.getSolrHome() );
		
		if ( solrHome.exists() ) {
			FileUtil.delete( solrHome );
		}
	}
	
	@Test
	public void getSolrHome() {
		assertEquals( "target/test-solr", helper.getSolrHome() );
	}
	
	@Test
	public void createSolrHomeDir() {
		helper.createSolrHomeDir();
		
		assertTrue( solrHome.exists() );
	}
	
	@Test
	public void installConfig() throws IOException {
		helper.installConfig();
		
		assertTrue( (new File( helper.getSolrHome() + File.separator + "solr.xml")).exists() );
	}
}
