mvn install -s settings.xml $* && mvn -pl webapp-statements jetty:run -s settings.xml $*
