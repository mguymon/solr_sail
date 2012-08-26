require 'rubygems'
require 'lock_jar'
require 'java'

class SolrSail

  # Find lockjar relative to the gem install
  def default_lockfile
    File.expand_path("#{File.dirname(__FILE__)}/Jarfile.lock")
  end
  
  def setup( opts )
    lockfile = opts.delete(:lockfile) || default_lockfile
    LockJar.install( lockfile, opts )
    LockJar.load( lockfile, opts )
    
    solr = com.tobedevoured.solrsail.Solr.new
    solr.createSolrHomeDir()
    solr.installConfig()
  end
end