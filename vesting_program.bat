echo %JAVA_HOME%
echo %1
echo %2
java -jar vestings/target/batch-processing-0.0.1-SNAPSHOT.jar --inputFile=%1 --fromDate=%2