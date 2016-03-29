name := "FinalProjectYelp"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.7"

val scalaTestVersion = "2.2.4"

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

//EclipseKeys.withSource := true

libraryDependencies ++= Seq(
	"org.apache.logging.log4j" % "log4j-api" % "2.4.1",
	"org.apache.logging.log4j" % "log4j-core" % "2.4.1",

	"org.apache.spark" %% "spark-core" % "1.5.1" % "provided",
	"org.apache.spark" %% "spark-streaming" % "1.5.1",
	"org.apache.spark" %% "spark-mllib" % "1.5.1",
	
	"com.fasterxml.jackson.core" % "jackson-databind" % "2.7.2"
)