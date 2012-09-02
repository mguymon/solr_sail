require 'rubygems'
require 'lock_jar'
require 'java'

module SolrSail
  @@version = IO.read("VERSION").strip
  @@jar = File.expand_path("#{File.dirname(__FILE__)}/../solrsail-#{@@version}.jar")
  @@lockfile = File.expand_path("#{File.dirname(__FILE__)}/../Jarfile.lock")
  
  def self.version
    @@version 
  end
  
  # Find lockjar relative to the gem install
  def self.default_lockfile
    @@lockfile
  end
  
  def self.default_jar
    @@jar
  end
  
  def self.install_config( opts )
    opts = { :jar => default_jar, :lockfile => default_lockfile }.merge( opts )
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
    opts = { :jar => default_jar, :lockfile => default_lockfile }.merge( opts )
    lockfile = opts.delete(:lockfile)
    jar = opts.delete(:jar)
        
    # XXX: should use LockJar to prevent duplicates in the classpath
    $CLASSPATH << File.expand_path( jar )
    
    LockJar.load( lockfile, opts )
    
    jetty = com.tobedevoured.solrsail.JettyServer.new
    jetty.start
  end
end