require 'rubygems'
require 'thor'
require 'solr_sail'

module SolrSail
  class CLI < Thor
    desc "install", "Install Solr config"
    method_option :solr_home,  :aliases => "-h", :default => 'solr', :desc => "Path to install solr config"
    def install
        SolrSail.install_config( :solr_home => options[:solr_home] )      
    end
    
    desc "start", "Run Solr"
    method_option :solr_home,  :aliases => "-h", :default => 'solr', :desc => "Path to install solr config"
    def start
        SolrSail.start( :solr_home => options[:solr_home] )   
    end
    
    desc "version", "Version"
    def version
      puts SolrSail::VERSION
    end
  end
end
