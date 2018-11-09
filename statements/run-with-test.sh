mvn install -s settings.xml $* && mvn -pl webapp jetty:run -s settings.xml $*
