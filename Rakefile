# encoding: utf-8

require 'rubygems'
require 'bundler'
require "fileutils"

begin
  Bundler.setup(:default, :development)
rescue Bundler::BundlerError => e
  $stderr.puts e.message
  $stderr.puts "Run `bundle install` to install missing gems"
  exit e.status_code
end

namespace :solr_sail do
  task :setup_target_gem do
      unless File.exists?( "target" )
        raise "Run `mvn package` to build java first" 
      end
      
      unless File.exists?( "target/gem" )
        Dir.mkdir( "target/gem" )
      end
      
      unless File.exists?( "target/gem/lib" )
        FileUtils.mkdir_p( "target/gem/lib/solr_sail" )
      end
      
      unless File.exists?( "target/gem/bin" )
         FileUtils.mkdir_p( "target/gem/bin" )
      end
      
      # copy ruby source over  
      Dir.glob("src/main/ruby/**/*.rb") do |file|
        FileUtils.copy( file, file.gsub("src/main/ruby", "target/gem/lib"))
      end
      
      # copy executable
      Dir.glob("src/main/bin/**") do |file|
            FileUtils.copy( file, file.gsub("src/main/bin", "target/gem/bin"))
      end
      
      FileUtils.copy( 'Gemfile', "target/gem/Gemfile" )
      FileUtils.copy( 'Gemfile.lock', "target/gem/Gemfile.lock" )
      FileUtils.copy( 'Jarfile', "target/gem/Jarfile" )
      FileUtils.copy( 'Jarfile.lock', "target/gem/Jarfile.lock" )
      FileUtils.copy( 'LICENSE', "target/gem/LICENSE" )
      FileUtils.copy( 'README.md', "target/gem/README.md" )
      FileUtils.copy( 'pom.xml', "target/gem/pom.xml" )
      FileUtils.copy( 'VERSION', "target/gem/VERSION" )
      FileUtils.copy( 'PostInstallRakefile', "target/gem/Rakefile" )
      
      version = IO.read('VERSION').strip
      FileUtils.copy( "target/solr_sail-#{version}.jar", "target/gem/solr_sail-#{version}.jar" )
      
      # Change dir so relevant files have the correct paths
      Dir.chdir( "target/gem" )      
  end
  
  task :version_return_from_target do
    FileUtils.copy( 'VERSION', "../../." )
    Dir.chdir( "../../" )
  end
  
  task :copy_gem_from_target do
    unless File.exists?( "../../pkg" )
      Dir.mkdir( "../../pkg" )
    end
    
    version = IO.read('VERSION').strip
    
    #source = File.expand_path("pkg/solr_sail-#{version}#{"-java" if platform =='java'}.gem")
        
    source = File.expand_path("pkg/solr_sail-#{version}-java.gem")
    dest = File.expand_path( "../../pkg/." )
    puts "copying #{source} to #{dest}"
    FileUtils.copy( source, dest )   
  end
end


require 'rake'

require 'jeweler'
Jeweler::Tasks.new do |gem|
  # gem is a Gem::Specification... see http://docs.rubygems.org/read/chapter/20 for more options
  gem.name = "solr_sail"
  gem.platform = "java"
  gem.homepage = "http://github.com/mguymon/solr_sail"
  gem.license = "MIT"
  gem.summary = %Q{From zero to search in the flash of a gem.}
  gem.description = %Q{From zero to search in the flash of a gem.}
  gem.email = "michael@tobedevoured.com"
  gem.authors = ["Michael Guymon"]
  gem.require_paths = %w[lib]
  gem.executable = "solrsail"
  gem.extensions = ["Rakefile"]
    
  # all files in target/gem should be included, except for pkg
  gem.files = Dir.glob("**/*").select{ |path| !(path =~ /^pkg/) }
end
Jeweler::RubygemsDotOrgTasks.new

Rake::Task["build"].enhance ["solr_sail:setup_target_gem"]
Rake::Task["version_required"].enhance ["solr_sail:setup_target_gem"]
Rake::Task["gemspec:release"].enhance ["solr_sail:setup_target_gem"]

Rake::Task["version:bump:major"].enhance ['solr_sail:version_return_from_target']
Rake::Task["version:bump:minor"].enhance ['solr_sail:version_return_from_target']
Rake::Task["version:bump:patch"].enhance ['solr_sail:version_return_from_target']

Rake::Task["build"].enhance do
  Rake::Task['solr_sail:copy_gem_from_target'].invoke
end

require 'rspec/core/rake_task'

RSpec::Core::RakeTask.new(:spec) do |t|
  t.pattern = 'src/test/spec/**/*_spec.rb'
end

task :test => :spec

task :default => :test

require 'rdoc/task'
Rake::RDocTask.new do |rdoc|
  version = File.exist?('VERSION') ? File.read('VERSION') : ""

  rdoc.rdoc_dir = 'rdoc'
  rdoc.title = "solr_sail #{version}"
  rdoc.rdoc_files.include('README*')
  rdoc.rdoc_files.include('src/main/ruby/**/*.rb')
end
