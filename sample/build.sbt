name := """sample"""

version := "1.0-SNAPSHOT"


lazy val root = (project in file(".")).dependsOn(file("../module")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
	javaJdbc,
	javaEbean,
	cache,
	javaWs,
	"org.webjars" % "jquery" % "2.1.1",
	"org.webjars" % "bootstrap" % "3.1.1",
	"org.webjars" % "font-awesome" % "4.1.0"
)

