require 'rubygems'
require 'lock_jar'
require 'java'

require 'solr_sail/version'

# :title:SolrSail
#
# Handles extracting the config and running an instance of Solr
#
# = Authors
# Michael Guymon
#
module SolrSail
  
  DEFAULT_JAR_NAME = "solr_sail-#{SolrSail::VERSION}.jar"
  DEFAULT_JAR = File.expand_path("#{File.dirname(__FILE__)}/../#{DEFAULT_JAR_NAME}")
  DEFAULT_LOCKFILE = File.expand_path("#{File.dirname(__FILE__)}/../Jarfile.lock")
  
  #
  # Install Jar dependencies, should be called by the Gem.post_install
  #
  def self.install_jars( opts ={} )
    opts = {:lockfile => DEFAULT_LOCKFILE }.merge( opts )
    lockfile = opts.delete(:lockfile)
    
    LockJar.install( lockfile, opts )
  end
  
  #
  # Extract the configuration files for Solr
  #
  # opts:
  #  * :jar: path to the solr_sail jar, default to jar packaged with the gem
  #  * :lockfile: path to lockfile that contains the jar dependencies, defaults to Jarfile.lock packaged with the gem
  #  * :solr_home: path to solr home, defaults to 'solr'
  def self.install_config( opts )
    opts = { :solr_home => 'solr', :jar => DEFAULT_JAR, :lockfile => DEFAULT_LOCKFILE }.merge( opts )
    lockfile = opts.delete(:lockfile)
    jar = opts.delete(:jar)
    
    # XXX: should use LockJar to prevent duplicates in the classpath
    $CLASSPATH << File.expand_path( jar )
    
    # XXX: need a safer way to pass opts to LockJar
    LockJar.load( lockfile, opts )
    
    solr_config = com.tobedevoured.solrsail.SolrConfig.new( opts[:solr_home] )
    solr_config.install()
  end
  
  #
  # Start Solr
  #
  # The server is configued by solr_sail.conf loaded from the solr home. The
  # opts passed in override solr_sail.conf
  #
  # opts:
  #  * :context_path: context path solr runs under, defaults to /solr
  #  * :jar: path to the solr_sail jar, default to jar packaged with the gem
  #  * :join: have the server join the thread. Defaults to false 
  #  * :lockfile: path to lockfile that contains the jar dependencies, defaults to Jarfile.lock packaged with the gem
  #  * :port: the port solr runs on, defaults to 8983
  #  * :solr_home: path to solr home, defaults to 'solr'
  #
  def self.start( opts )
    opts = { 
      :solr_home => 'solr', :jar => DEFAULT_JAR, :lockfile => DEFAULT_LOCKFILE, :join => false }.merge( opts )
    
    lockfile = opts.delete(:lockfile)
    jar = opts.delete(:jar)
        
    # XXX: should use LockJar to prevent duplicates in the classpath
    $CLASSPATH << File.expand_path( jar )
    
    # XXX: need a safer way to pass opts to LockJar
    LockJar.load( lockfile, opts )
    
    jetty = com.tobedevoured.solrsail.JettyServer.new( opts[:solr_home] )
    
    if opts[:port]
      jetty.setPort( opts[:port] )
    end
    
    if opts[:context_path]
      jetty.setContextPath(opts[:context_path])
    end
    
    jetty.start(opts[:join])
      
    jetty
  end
end