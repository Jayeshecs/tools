mvn install -s settings.xml -DskipUTs -DskipITs -DskipTests=true $* && mvn -s settings.xml -pl webapp-statements jetty:run $*
