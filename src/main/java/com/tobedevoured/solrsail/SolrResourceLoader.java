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
