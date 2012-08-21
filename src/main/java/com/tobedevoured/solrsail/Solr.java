package com.tobedevoured.solrsail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metapossum.utils.scanner.PackageScanner;


import com.tobedevoured.command.Runner;
import com.tobedevoured.command.annotation.ByYourCommand;
import com.tobedevoured.command.annotation.Command;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@ByYourCommand
public class Solr {
	private static Logger logger = LoggerFactory.getLogger( Solr.class );
	
	private String solrHome;
	
	public Solr() {
		Config config = ConfigFactory.load();
		solrHome = config.getString("solrsail.solr.home");
	}
	
	public Solr( String solrHome ) {
		this.solrHome = solrHome;
	}
	
	public String getSolrHome() {
		return solrHome;
	}
	
	public void createSolrHomeDir() {
		File file = new File( solrHome );
		if ( !file.exists() ) {
			file.mkdirs();
		}
	}
	
	/**
	 * Explode the solr config from the classpath
	 * 
	 * @throws IOException
	 */
	@Command
	public void installConfig() throws IOException {
		for ( String path : findSolrInClasspath() ) {
			if ( path.startsWith( "solr/") && !path.endsWith("/") ) {
				final InputStream inputStream = getClass().getClassLoader().getResourceAsStream( path );
				
				// Remove the solr prefix from the path
				final String destPath = path.substring(5);
				final File destFile = new File( 
					new StringBuilder(solrHome).append( File.separator ).append( destPath ).toString() );
				
				logger.debug( "Copying {} to {}", path, destFile );
				
				if ( !destFile.getParentFile().exists() ) {
					destFile.getParentFile().mkdirs();
				}
				
				final FileOutputStream outputStream = new FileOutputStream( destFile );
				
				/*
				// expands tokens on xml configs
				if ( destPath.endsWith( ".xml" ) ) {
					String xml = IOUtils.toString( inputStream );
					if ( xml.contains("${solr.home}") ) {
						logger.debug( "Replacing ${solr.home} in {}", destPath );
						xml = xml.replaceAll("\\$\\{solr.home\\}", ( new File( solrHome ) ).getAbsolutePath() );
						IOUtils.write( xml, outputStream );
					}
				} else {
					IOUtils.copy( inputStream, outputStream ); 
				}
				*/
				IOUtils.copy( inputStream, outputStream ); 
				
				outputStream.close();
				inputStream.close();
			} else {
				logger.debug( "skipping {}", path );
			}
		}
	}
	
	/**
	 * Find Solr dir in the classpath
	 * 
	 * @return Set<String> of paths
	 * @throws IOException 
	 */
	public Set<String> findSolrInClasspath() throws IOException {
		PackageScanner<String> scanner = new PackageScanner<String>( new SolrResourceLoader( this.getClass().getClassLoader() ) );
		scanner.setRecursive( true );
		
		return scanner.scan( "solr" );
	}
	
	public static void main( String[] args ) throws Exception {
    	Runner.run(args);
    }
}
