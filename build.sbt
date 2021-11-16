name := "scala_plugin_test_project"

version := "0.1"

scalaVersion := "2.13.7"

libraryDependencies += "com.lihaoyi" %% "upickle" % "1.4.2"

libraryDependencies += "com.lihaoyi" %% "utest" % "0.7.10" % "test"

testFrameworks += new TestFramework("utest.runner.Framework")
