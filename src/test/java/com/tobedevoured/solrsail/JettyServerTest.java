package com.tobedevoured.solrsail;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;

public class JettyServerTest {

	@Test
	public void useLocalConfig() {
		JettyServer server = new JettyServer( "src/test/resources" );
		
		assertEquals( "src/test/resources", server.getSolrHome() );
		assertEquals( Integer.valueOf( 9999 ), server.getPort() );
		assertEquals( "/test", server.getContextPath() );
	}
	
	@Test
	public void start() throws Exception {
		File configDir = new File("target/solr");
		
		if ( configDir.exists() ) {
			FileUtils.forceDelete( configDir );
		}
		
		SolrConfig config = new SolrConfig( "target/solr" );
		config.install();
		
		assertTrue( configDir.exists() );
		
		JettyServer server = new JettyServer( "target/solr" );
		server.start();
		
		SolrServer solr = new HttpSolrServer("http://localhost:8983/solr");
		SolrQuery query = new SolrQuery();
		query.setQuery( "*:*" );
		
		QueryResponse response = solr.query( query );
		
		assertEquals( "{numFound=0,start=0,docs=[]}", response.getResponse().get("response").toString() );
	}
}
