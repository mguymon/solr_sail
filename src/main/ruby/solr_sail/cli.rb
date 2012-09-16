require 'rubygems'
require 'thor'
require 'solr_sail'

module SolrSail
  class CLI < Thor
    desc "install", "Install Solr config"
    method_option :solr_home,  :aliases => "-h", :default => 'solr', :desc => "Path to install solr config"
    def install
        puts "Installing Solr config"
        SolrSail.install_config( :solr_home => options[:solr_home] )      
    end
    
    desc "install_jars", "Install Solr jar dependencies"
    def install_jars
        puts "Installing Jar dependencies"
        SolrSail.install_jars()      
    end
    
    desc "start", "Run Solr"
    method_option :solr_home,  :aliases => "-h", :default => 'solr', :desc => "Path to install solr config"
    method_option :port, :aliases => "-p", :type => :integer, :desc => "Port to run Solr on, overrides Solr Home config"
    def start
        SolrSail.start( :solr_home => options[:solr_home], :port => options[:port] )   
    end
    
    desc "version", "Version"
    def version
      puts SolrSail::VERSION
    end
  end
end
