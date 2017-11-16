name := """scala-sangria-basic-impl"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += "org.sangria-graphql" %% "sangria" % "1.3.2"
libraryDependencies += "org.sangria-graphql" %% "sangria-marshalling-api" % "1.0.0"
