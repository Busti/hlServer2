name := "hlServer2"
version := "0.2.0"
scalaVersion in ThisBuild := "2.12.1"

run <<= run in Compile in core

lazy val macros = (project in file("macros")).settings(
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
)

lazy val core = (project in file("core")).settings(
  libraryDependencies ++= Seq(
    "com.github.nscala-time" %% "nscala-time" % "2.16.0",
    "org.http4s" %% "http4s-dsl" % "0.17.2",
    "org.http4s" %% "http4s-blaze-server" % "0.17.2",
    "org.http4s" %% "http4s-blaze-client" % "0.17.2",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )
) dependsOn macros