package com.tobedevoured.solrsail;

import java.io.File;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.metapossum.utils.scanner.ResourceLoader;

/**
 * Searches the classpath for the solr directory
 * 
 * @author Michael Guymon
 */
public class SolrResourceLoader implements ResourceLoader<String> {

	private static final Logger logger = LoggerFactory.getLogger( SolrResourceLoader.class );
	
	protected ClassLoader classLoader;

	public SolrResourceLoader(final ClassLoader classLoader) {
		logger.debug( "init" );
		this.classLoader = classLoader;
	}

	public String loadFromJarfile(final String packageName, final JarEntry entry) {
		return loadFromFile(packageName, entry.getName());
	}
	
	public String loadFromJarfile(String arg0, JarFile arg1, JarEntry arg2) {
		return null;
	}

	public String loadFromFilesystem(final String packageName, final File directory, final String fileName) {
		return loadFromFile(packageName, fileName);
	}

	protected String loadFromFile(final String packageName, final String fileName) {
		
		//logger.debug( "{} {}", packageName, fileName );
		
		// XXX: Hack to fix production packageName duplicated in fileName
		final String path = packageName.replace( ".", "/");
		if ( !packageName.equals( fileName ) && !fileName.contains( path + "/" ) && !fileName.contains(packageName + "/") ) {
			return new StringBuilder( path ).append( "/" ).append( fileName ).toString();
		} else {
			return fileName;
		}
	}

}
