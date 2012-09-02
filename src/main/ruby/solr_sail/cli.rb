require 'rubygems'
require 'thor'
require 'solr_sail'

module SolrSail
  class CLI < Thor
    desc "install", "Install Solr config"
    method_option :dest,  :aliases => "-d", :default => 'solr', :desc => "Path to install solr config"
    def install
        SolrSail.install_config( :dest => options[:dest] )      
    end
    
    desc "start", "Run Solr"
    method_option :dest,  :aliases => "-d", :default => 'solr', :desc => "Path to install solr config"
    def start
        SolrSail.start( :dest => options[:dest] )   
    end
  end
end
