# SmartGWT SETUP

## MAVEN SETUP

- Install maven 3.5.2+ from https://maven.apache.org/install.html

## GWT INSTALL

mvn archetype:generate  -DarchetypeGroupId=org.codehaus.mojo  -DarchetypeArtifactId=gwt-maven-plugin  -DarchetypeVersion=2.8.2 -DartifactId=restful-sgwt -Dmodule=RestfullWeb 
-DgroupId=org.apache.isis.viewer -Dpackage=org.apache.isis.viewer.sgwt

## SMARTGWT INSTALL

mvn com.isomorphic:isc-maven-plugin:1.3.0:install -Dproduct=SMARTGWT -Dlicense=LGPL -DbuildNumber=12.0p

REF: https://www.smartclient.com/smartgwtee-release/javadoc/com/smartgwt/client/docs/MavenSupport.html

## SMARTGWT SUPERDEV MODE

REF: https://www.smartclient.com/smartgwt/javadoc/com/smartgwt/client/docs/SuperDevModeTroubleshooting.html