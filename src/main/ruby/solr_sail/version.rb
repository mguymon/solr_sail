module SolrSail
    
  # Load VERSION file to VERSION var
  if File.exists?( File.expand_path("#{File.dirname(__FILE__)}/../VERSION") )
    VERSION = IO.read(File.expand_path("#{File.dirname(__FILE__)}/../VERSION")).strip
      
  # VERSION file not found in gem dir, assume running from checkout
  else
    VERSION = IO.read(File.expand_path("VERSION")).strip
  end
end