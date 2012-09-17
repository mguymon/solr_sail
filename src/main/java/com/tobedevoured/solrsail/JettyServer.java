package com.tobedevoured.solrsail;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
   *
 * http://www.apache.org/licenses/LICENSE-2.0
   *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.plus.jndi.EnvEntry;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.tobedevoured.command.LogUtil;
import com.tobedevoured.command.Runner;
import com.tobedevoured.command.annotation.ByYourCommand;
import com.tobedevoured.command.annotation.Command;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Jetty Server
 *
 * @author Michael Guymon
 */
@ByYourCommand
public class JettyServer {
	private static Logger logger = LoggerFactory.getLogger( JettyServer.class );
	
	private String solrHome;
	private String contextPath; 
	private Integer port;
	private Server server;
	
	/**
	 * Create new instance
	 */
	public JettyServer( String solrHome ) {
		this.solrHome = solrHome;
		
		Config config = ConfigFactory.load();
		
		File localConfigFile = new File( new StringBuilder( this.solrHome ).append( File.separator ).append( "solr_sail.conf" ).toString() );
		if ( localConfigFile.exists() ) {
			Config localConfig = ConfigFactory.parseFile( localConfigFile );
			config = localConfig.withFallback( config );
		}
		
		port = config.getInt("solr_sail.solr.port");
		contextPath = config.getString("solr_sail.solr.contextpath");
	}
	
	/**
	 * Start Jetty
	 * 
	 * @throws Exception
	 */
	@Command
	public void start() throws Exception {
		start(false);
	}
	
	/**
	 * Start Jetty
	 * 
	 * @param join boolean to join the thread
	 * @throws Exception
	 */
    public void start(boolean join) throws Exception {
		
    	System.setProperty("java.naming.factory.url.pkgs", "org.eclipse.jetty.jndi");
    	System.setProperty("java.naming.factory.initial", "org.eclipse.jetty.jndi.InitialContextFactory");
    	
    	System.setProperty("org.apache.jasper.compiler.disablejsr199", "true" );
    	
    	File logbackConfig = new File( getSolrHome() + File.separator + "logback.xml" );
    	if ( logbackConfig.exists() ) {
    		LogUtil.configureFromFile( logbackConfig );
    	}
    	
    	server = new Server(getPort());
    	
    	String webDir = JettyServer.class.getClassLoader().getResource("webapp").toExternalForm();

    	EnvEntry env = new EnvEntry("java:comp/env/solr/home", getSolrHome() );
    	
        WebAppContext context = new WebAppContext();
        context.setDisplayName("Solr");
        context.setDescriptor( webDir +"/WEB-INF/web.xml" );
        context.setResourceBase( webDir );
        context.setContextPath( getContextPath() );
        context.setParentLoaderPriority(true);
 
        server.setHandler(context);
 
        logger.info( "starting Jetty" );
        
        server.start();
        
        if ( join ) {
        	logger.info( "Joining thread" );
        	server.join();
        }
    }
    
    /**
     * Stop Jetty
     * 
     * @throws Exception
     */
    public void stop() throws Exception {
    	server.stop();
    }
	
	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getSolrHome() {
		return solrHome;
	}
	
    public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public static void main( String[] args ) throws Exception {
    	Runner.run(args);
    }

}
