# webcrawler

Current Build Status
![Travis Build Image]
(https://travis-ci.org/rayram23/webcrawler.svg?branch=master)


The app is built in Java and uses maven for build and dependency management. Please use your package manager of choice to install maven.
brew install maven
apt-get install maven
yum install maven



#How to run
From the project directory run the following maven command:
mvn exec:java -Dexec.mainClass="com.rayram23.webcrawler.App" -Dexec.args="http://digitalocean.com 1 10"

#How to run tests
From the project directory run the following command:
mvn cobertura:cobertura

#View Code Coverage
After running test a code coverage html site is built. Navigate to the below file with a broswer to view code coverage.
target/site/cobertura/index.html 
 
