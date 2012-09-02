require 'rubygems'
require 'lock_jar'
require 'java'

require 'solr_sail/version'

module SolrSail
  
  DEFAULT_JAR_NAME = "solr_sail-#{SolrSail::VERSION}.jar"
  DEFAULT_JAR = File.expand_path("#{File.dirname(__FILE__)}/../#{DEFAULT_JAR_NAME}")
  DEFAULT_LOCKFILE = File.expand_path("#{File.dirname(__FILE__)}/../Jarfile.lock")
  
  def self.install_config( opts )
    opts = { :jar => DEFAULT_JAR, :lockfile => DEFAULT_LOCKFILE }.merge( opts )
    lockfile = opts.delete(:lockfile)
    jar = opts.delete(:jar)
    
    # XXX: should use LockJar to prevent duplicates in the classpath
    $CLASSPATH << File.expand_path( jar )
    
    LockJar.install( lockfile, opts )
    LockJar.load( lockfile, opts )
    
    solr_config = com.tobedevoured.solrsail.SolrConfig.new( opts[:dest] )
    solr_config.install()
  end
  
  def self.start( opts )
    opts = { :jar => DEFAULT_JAR, :lockfile => DEFAULT_LOCKFILE, :join => true }.merge( opts )
    lockfile = opts.delete(:lockfile)
    jar = opts.delete(:jar)
        
    # XXX: should use LockJar to prevent duplicates in the classpath
    $CLASSPATH << File.expand_path( jar )
    
    LockJar.load( lockfile, opts )
    
    jetty = com.tobedevoured.solrsail.JettyServer.new()
    jetty.start(opts[:join])
      
    jetty
  end
end