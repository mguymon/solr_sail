# Solr Sail

From zero to search in the flash of a gem.

## Install

### For Ruby

    gem install solr_sail

### For Java

    <dependency>
      <groupId>com.tobedevoured</groupId>
      <artifactId>solrsail</artifactId>
      <version>0.0.2</version>
    </dependency>
    
## Usage

### From the command line

The _solr_sail_ gem comes with the executable solrsail which makes it easy to setup 
and start a Solr Server.

#### Setup Solr

To create a config directory in the default path of **solr**, simply run:

    solrsail install
    
To change the directory, you can pass _--solr_home_ or _-h_:
  
    solrsail install -h new/path/to/solr
    
If you want to make changes to how the Solr Server runs, you can edit the 
[conf](https://github.com/typesafehub/config) _solr_sail.conf_ under the installed
Solr home. An example _solr_sail.conf_:

    solr_sail.solr.port=8983
    solr_sail.solr.contextpath=/solr
    
#### Start up Solr

    solrsail start
    
To change the directory of the Solr home, you can pass _--solr_home_ or _-h_:
  
    solrsail start -h new/path/to/solr
    
### From Ruby

#### Setup Solr

To create a config directory

    require 'rubygems'
    require 'solr_sail'
    
    SolrSail.install_config()

#### Start up Solr    

    require 'rubygems'
    require 'solr_sail'
    
    SolrSail.start()
      
    