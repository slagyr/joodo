lein_exe = "lein2"

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


DIRS = %w{chee joodo lein_joodo}

DIRS.each do |dir|

  namespace dir do
    desc "full #{dir} build"
    task :build do
      in_dir dir do
        run_command "#{lein_exe} deps"
        run_command "#{lein_exe} javac"
        run_command "#{lein_exe} spec"
      end
    end

    desc "push to clojars"
    task :push do
      in_dir dir do
        run_command "#{lein_exe} jar"
        run_command "#{lein_exe} push"
      end
    end

    desc "install locally"
    task :install do
      in_dir dir do
        run_command "#{lein_exe} install"
      end
    end
  end

end

desc "Build all projects"
task :build => %w{chee:build joodo:build lein_joodo:build}

desc "Push all projects to clojars"
task :push => %w{chee:push joodo:push lein_joodo:push}

desc "Install all built jars locally"
task :install => %w{chee:install joodo:install lein_joodo:install}

task :default => :build