name := """play2-batch"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

organization := "kr.bnnb"

organizationName := "BNNB"

organizationHomepage := Some(new URL("http://bnnb.kr"))

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)
