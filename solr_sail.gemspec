# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = "solr_sail"
  s.version = "0.1.0"
  s.platform = "java"

  s.require_paths = ["lib"]
  s.authors = ["Michael Guymon"]
  s.date = "2014-06-27"
  s.description = "From zero to search in the flash of a gem."
  s.email = "michael@tobedevoured.com"
  s.executables = ["solrsail"]
  s.extensions = ["Rakefile"]
  s.extra_rdoc_files = [
    "LICENSE",
    "README.md"
  ]
  s.files = [
    "Gemfile",
    "Gemfile.lock",
    "Jarfile",
    "Jarfile.lock",
    "LICENSE",
    "README.md",
    "Rakefile",
    "VERSION",
    "bin/solrsail",
    "lib/solr_sail.rb",
    "lib/solr_sail/cli.rb",
    "lib/solr_sail/version.rb",
    "pom.xml",
    "solr_sail-0.1.0.jar",
    "solr_sail.gemspec"
  ]
  s.homepage = "http://github.com/mguymon/solr_sail"
  s.licenses = ["MIT"]
  s.rubygems_version = "2.2.2"
  s.summary = "From zero to search in the flash of a gem."

  s.add_dependency(%q<lock_jar>, [">= 0.7.0"])

end

