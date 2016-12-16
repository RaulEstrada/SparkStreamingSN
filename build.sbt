name := "SocialMediaStream"
version := "1.0"
scalaVersion := "2.11.7"

assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.last
}
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
assemblyOption in assembly := (assemblyOption in assembly).value.copy(cacheOutput = false)

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.0.1" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.0.1" % "provided"
libraryDependencies += "org.apache.bahir" %% "spark-streaming-twitter" % "2.0.1"
libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.1"
libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.1" classifier "models"
libraryDependencies += "com.ibm.watson.developer_cloud" % "tone-analyzer" % "3.5.2"
libraryDependencies += "org.elasticsearch" % "elasticsearch-spark-20_2.11" % "5.1.1"
libraryDependencies += "joda-time" % "joda-time" % "2.9.6"
libraryDependencies += "org.joda" % "joda-convert" % "1.8.1"

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"
