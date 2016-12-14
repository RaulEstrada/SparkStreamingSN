package main.scala

import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.twitter._

object Driver {
    val SECONDS = 20

    def main(args: Array[String]) {
        val conf = new SparkConf()
        val context = new StreamingContext(conf, Seconds(SECONDS))
        val twitterStream = TwitterUtils.createStream(context, None)
    }

}
