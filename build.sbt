val scala3Version = "3.2.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "PuduCalc",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    scalacOptions ++= Seq("-Yretain-trees"),
    resolvers += Resolver.githubPackages("jsnavar"),
    libraryDependencies ++= Seq("org.scalameta" %% "munit" % "0.7.29" % Test,
                                "pudu" %% "pudu" % "0.1.2")
  )
