name := "SocialMediaStream"
version := "1.0"
scalaVersion := "2.11.7"

assemblyMergeStrategy in assembly :=
   {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
   }

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.0.1" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.0.1" % "provided"
libraryDependencies += "org.apache.bahir" %% "spark-streaming-twitter" % "2.0.1"
