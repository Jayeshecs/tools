mvn install -DskipUTs -DskipITs -DskipTests=true -s settings.xml $* && mvn -pl webapp-statements jetty:run -s settings.xml $*
