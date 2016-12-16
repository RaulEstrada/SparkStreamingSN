package main.scala.schema

import org.joda.time.DateTime

case class Tweet(val username: String, val creationDate: DateTime, val tweet: String,
    val locationLat: Option[Double], val locationLong: Option[Double],
    var emotionMap: Option[scala.collection.mutable.Map[String, Double]] = None,
    var languageToneMap: Option[scala.collection.mutable.Map[String, Double]] = None,
    var socialToneMap: Option[scala.collection.mutable.Map[String, Double]] = None,
    var stanfSentiment: Option[Int] = None) extends Serializable {

}
