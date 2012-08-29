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
    
    $CLASSPATH << File.expand_path( jar )
    LockJar.install( lockfile, opts )
    LockJar.load( lockfile, opts )
    
    solr = com.tobedevoured.solrsail.SolrConfig.new
    solr.install()
  end
end