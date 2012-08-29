package com.tobedevoured.solrsail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metapossum.utils.scanner.PackageScanner;

import com.tobedevoured.command.Runner;
import com.tobedevoured.command.annotation.ByYourCommand;
import com.tobedevoured.command.annotation.Command;
import com.tobedevoured.command.annotation.CommandParam;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@ByYourCommand
public class SolrConfig {
	private static Logger logger = LoggerFactory.getLogger( SolrConfig.class );
	
	private String solrHome;
	
	public SolrConfig() {
		Config config = ConfigFactory.load();
		solrHome = config.getString("solrsail.solr.home");
	}
	
	public SolrConfig( String solrHome ) {
		this.solrHome = solrHome;
	}
	
	public String getSolrHome() {
		return solrHome;
	}
	
	/**
	 * Explode the solr config from the classpath
	 * 
	 * @throws IOException
	 */
	@Command
	public void installFromClasspath() throws IOException {
		logger.info( "Installing config from classpath to {}", this.getSolrHome() );
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
	
	@Command
	public void install() throws IOException, URISyntaxException {
		File jar = new File( this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI() );
		
		if ( jar.toURI().getPath().endsWith( ".jar") && jar.exists() ) {
			installFromJar( jar );
		} else {
			installFromClasspath();
		}
	}
	
	@Command
	@CommandParam(name = "jar", type = String.class)
	public void installFromJar( String jar ) throws IOException, URISyntaxException {
		installFromJar( new File( jar ) );
	}
		
	public void installFromJar( File jar ) throws IOException {
		logger.info( "Installing config from Jar to {}", this.getSolrHome() );
		logger.debug( "Opening Jar {}", jar.toString() );
		
		JarFile jarFile = new JarFile( jar );
		
		for( Enumeration<JarEntry> enumeration = jarFile.entries(); enumeration.hasMoreElements(); ) {
			JarEntry entry = enumeration.nextElement();
			
			if ( !entry.getName().equals( "solr/") && entry.getName().startsWith( "solr/") ) {
				StringBuilder dest = new StringBuilder( getSolrHome() )
					.append( File.separator )
					.append( entry.getName().replaceFirst("solr/", "") );
				
				File file = new File( dest.toString() );
				
				if ( entry.isDirectory() ) {
					file.mkdirs();
				} else {
					if ( file.getParentFile() != null ) {
						file.getParentFile().mkdirs();
					}
					
					logger.debug( "Copying {} to {}", entry.getName(), dest.toString() );
					
					InputStream input = jarFile.getInputStream( entry );
					Writer writer =  new FileWriter( file.getAbsoluteFile() );
					IOUtils.copy( input, writer );
					
					input.close();
					
					writer.close();
				}
			}
		}
	}
	
	public static void main( String[] args ) throws Exception {
    	Runner.run(args);
    }
}
