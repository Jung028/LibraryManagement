ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.17"

lazy val root = (project in file("."))
  .settings(
    name := "myscalafxproject",
    libraryDependencies ++= Seq("org.scalafx" %% "scalafx" % "8.0.192-R14")
  )

