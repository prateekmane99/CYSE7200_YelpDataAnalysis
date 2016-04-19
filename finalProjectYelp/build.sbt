name := "FinalProjectYelp"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.7"

val scalaTestVersion = "2.2.4"

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

//EclipseKeys.withSource := true

libraryDependencies ++= Seq(

	//Logging
	"org.apache.logging.log4j" % "log4j-api" % "2.4.1",
	"org.apache.logging.log4j" % "log4j-core" % "2.4.1",
	
	
	//Scala Testing
  	"org.scalatest"   %% "scalatest"    % "2.2.4",
  	"org.scalacheck"  %% "scalacheck"   % "1.12.2",


	//Spark
	"org.apache.spark" %% "spark-core" % "1.5.1" % "provided",
	"org.apache.spark" %% "spark-streaming" % "1.5.1",
	"org.apache.spark" %% "spark-mllib" % "1.5.1",


	//Stanford 
	"edu.stanford.nlp" % "stanford-corenlp" % "3.5.1",
  	"edu.stanford.nlp" % "stanford-corenlp" % "3.5.1" classifier "models",
 
  
  	//Word Cloud
	 "com.kennycason" % "kumo" % "1.3", 
	 
	 
	 //JSON Parsing
	 "com.typesafe.play" %% "play-json" % "2.4.0-M3",
	 
	 
	 "org.skife.com.typesafe.config" % "typesafe-config" % "0.3.0",
	 
	 
	 //PDF Generation
	 "com.itextpdf" % "itextpdf" % "5.5.0",
   	 "com.itextpdf.tool" % "xmlworker" % "5.4.4"
   	 
   	 
   	 // Convert Dataframe TO CSV
  	//"com.databricks" % "spark-csv_2.10" % "1.2.0"
  	
  	
)