require 'rubygems'
require 'rsolr'
require  File.expand_path(File.join(File.dirname(__FILE__), 'spec_helper'))
require 'src/main/ruby/solr_sail'
require 'src/main/ruby/solr_sail/version'
require 'fileutils'

describe SolrSail do
  describe "Module" do
    it "should have a DEFAULT_LOCKJAR" do
      SolrSail::DEFAULT_LOCKFILE.should eql File.expand_path( "src/main/Jarfile.lock" )
    end
    
    it "should have a DEFAULT JAR NAME" do
      SolrSail::DEFAULT_JAR_NAME.should eql "solr_sail-#{SolrSail::VERSION}.jar"
    end
    
    it "should have a DEFAULT_JAR" do
      SolrSail::DEFAULT_JAR.should eql File.expand_path( "src/main/#{SolrSail::DEFAULT_JAR_NAME}" )
    end
    
    it "should install config" do
      if File.exists? 'tmp/solr'
        FileUtils.rm_rf( 'tmp/solr' )
      end
      
      SolrSail.install_config( :dest => 'tmp/solr', :lockfile => 'Jarfile.lock', :jar => "target/#{SolrSail::DEFAULT_JAR_NAME}" )
      
      File.exists?( 'tmp/solr/solr.xml').should be_true
      File.exists?( 'tmp/solr/conf').should be_true
    end
  
    context "start" do
      before(:all) do
        @server = SolrSail.start( :join => false, :dest => 'tmp/solr', :lockfile => 'Jarfile.lock', :jar => "target/#{SolrSail::DEFAULT_JAR_NAME}" )
        @solr = RSolr.connect( :url => 'http://localhost:8080/solr' )
      end

      after(:all) do
        @server.stop()
      end
      
      it "should be able to connect" do
        @solr = RSolr.connect :url => 'http://localhost:8080/solr'
      end
      
      it "should be able to add and query a document" do
        @solr.add :id=>1, :price=>1.00
        
        result  = @solr.get('select', :params => {:q=>'id:1'})
        result["response"]["docs"].should eql [{"id"=>"1", "price"=>1.0, "price_c"=>"1.0,USD"}]
      end
      
    end
  end
end