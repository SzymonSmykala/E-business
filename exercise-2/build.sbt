name := "exercise2"
 
version := "1.0" 
      
lazy val `exercise2` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.12.8"
libraryDependencies += "com.typesafe.play" % "play-json-joda_2.12" % "2.6.0"
libraryDependencies ++= Seq( ehcache , ws , specs2 % Test , guice )
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
  "org.xerial"        %  "sqlite-jdbc" % "3.30.1",
  "org.scalaj" % "scalaj-http_2.12" % "2.4.2",
  "com.jason-goodwin" %% "authentikat-jwt" % "0.4.5",
  "org.mindrot" % "jbcrypt" % "0.3m"
)
unmanagedResourceDirectories in Test +=  baseDirectory ( _ /"target/web/public/test" ).value
//unmanagedResourceDirectories in Test +=  baseDirectory ( _ /"target/web/public/test" )
//
