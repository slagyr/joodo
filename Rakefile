def run_command(command)
  system command
  exit_code = $?.exitstatus
  if exit_code != 0
    raise "Command failed with code #{exit_code}: #{command}"
  else
    puts "Command executed successfully: #{command}"
  end
end

def in_dir(path)
  pwd = Dir.getwd
  Dir.chdir path
  yield
ensure
  Dir.chdir pwd
end

namespace :joodo do
  desc "full joodo build"
  task :build do
    in_dir "joodo" do
      run_command "lein deps, javac"
      run_command "lein spec"
    end
  end
end

namespace :lein_joodo do

  desc "init lein-joodo"
  task :init do
    in_dir "lein-joodo" do
      if !File.exists?("leiningen-1.7.0-standalone.jar")
        puts "downloading Leiningen"
        run_command "wget https://github.com/downloads/technomancy/leiningen/leiningen-1.7.0-standalone.jar"
      else
        puts "Leiningen already downloaded"
      end
    end
  end

  desc "full lein-joodo build"
  task :build => %w{init} do
    in_dir "lein-joodo" do
      run_command "lein deps, javac"
      run_command "lein spec"
    end
  end
end

desc "full build"
task :build => %w{joodo:build lein_joodo:build}

task :default => :build