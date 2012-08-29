require 'rubygems'
require 'lock_jar'
require 'java'

class SolrSail
  @@version = IO.read("VERSION").strip
  @@jar = File.expand_path("#{File.dirname(__FILE__)}/solrsail-#{@@version}.jar")
  
  def version
    @@version 
  end
  
  # Find lockjar relative to the gem install
  def default_lockfile
    File.expand_path("#{File.dirname(__FILE__)}/Jarfile.lock")
  end
  
  def default_jar
    @@jar
  end
  
  def install_config( opts )
    opts = { :jar => default_jar, :lockfile => default_lockfile }.merge( opts )
    lockfile = opts.delete(:lockfile)
    jar = opts.delete(:jar)
    
    # XXX: should use LockJar to prevent duplicates in the classpath
    $CLASSPATH << File.expand_path( jar )
    
    LockJar.install( lockfile, opts )
    LockJar.load( lockfile, opts )
    
    solr_config = com.tobedevoured.solrsail.SolrConfig.new
    solr_config.install()
  end
  
  def run( opts )
    opts = { :jar => default_jar, :lockfile => default_lockfile }.merge( opts )
    lockfile = opts.delete(:lockfile)
    jar = opts.delete(:jar)
        
    # XXX: should use LockJar to prevent duplicates in the classpath
    $CLASSPATH << File.expand_path( jar )
    
    LockJar.load( lockfile, opts )
    
    jetty = com.tobedevoured.solrsail.JettyServer.new
    jetty.run
  end
end