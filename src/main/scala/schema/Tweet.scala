package main.scala.schema

import java.util.Date

case class Tweet(val username: String, val creationDate: Date, val tweet: String,
    val locationLat: Option[Double], val locationLong: Option[Double]) {

}
