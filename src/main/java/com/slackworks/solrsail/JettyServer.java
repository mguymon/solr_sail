package com.slackworks.solrsail;

import java.io.IOException;

import org.eclipse.jetty.plus.jndi.EnvEntry;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slackworks.command.ConfigFactory;
import com.slackworks.command.Runner;
import com.slackworks.command.annotation.ByYourCommand;
import com.slackworks.command.annotation.Command;
import com.typesafe.config.Config;

/**
 * Jetty Server
 *
 */
@ByYourCommand
public class JettyServer {
	private static Logger logger = LoggerFactory.getLogger( JettyServer.class );
	
	private String solrHome = "solr";
	private String contextPath = "/solr"; 
	private int port = 8080;
	
	public JettyServer() throws IOException {
		Config config = ConfigFactory.load();
		solrHome = config.getString("solrsail.solr.home");
	}
	
	@Command
    public void run() throws Exception {
		
    	System.setProperty("java.naming.factory.url.pkgs", "org.eclipse.jetty.jndi");
    	System.setProperty("java.naming.factory.initial", "org.eclipse.jetty.jndi.InitialContextFactory");
    	
    	System.setProperty("org.apache.jasper.compiler.disablejsr199", "true" );
    	
    	Server server = new Server(getPort());
    	
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
        server.join();
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

	public void setSolrHome(String solrHome) {
		this.solrHome = solrHome;
	}
	
    public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public static void main( String[] args ) throws Exception {
    	Runner.run(args);
    }

}
