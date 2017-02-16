name := "correct_ADV"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.github.tototoshi" % "scala-csv_2.10" % "1.3.4",
  "org.apache.spark" %% "spark-core" % "2.1.0",
  "org.apache.spark" %% "spark-sql" % "2.1.0",
  "commons-httpclient" % "commons-httpclient" % "3.1",
  "org.apache.httpcomponents" % "httpclient" % "4.5.3",
  "io.spray" %%  "spray-json" % "1.3.3",
  "io.spray" %% "spray-http" % "1.3.3",
  "io.spray" %% "spray-httpx" % "1.3.3",
  "com.typesafe" % "config" % "1.3.1",
  "com.github.sstone" %% "amqp-client" % "1.5",
  "com.typesafe.akka" %% "akka-actor" % "2.4.17",
  "com.gingersoftware" %% "object-csv" % "0.3"
)
