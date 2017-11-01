name := "hlServer2"
version := "0.2.0"
scalaVersion in ThisBuild := "2.12.1"

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time"         % "2.16.0",
  "org.http4s"             %% "http4s-dsl"          % "0.17.2",
  "org.http4s"             %% "http4s-blaze-server" % "0.17.2",
  "org.http4s"             %% "http4s-blaze-client" % "0.17.2",
  "ch.qos.logback"         %  "logback-classic"     % "1.2.3",
  "de.sciss"               %% "scalaosc"            % "1.1.5",
  "com.lihaoyi"            %% "scalarx"             % "0.3.2"
)
