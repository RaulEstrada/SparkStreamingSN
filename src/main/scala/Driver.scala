package main.scala

import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.twitter._

object Driver {
    val SECONDS = 20

    def main(args: Array[String]) {
        if (args.length < 1) {
            println("SocialMediaStream Driver usage: <word>")
            System.exit(-1)
        }
        val conf = new SparkConf()
        val context = new StreamingContext(conf, Seconds(SECONDS))
        val twitterStream = TwitterUtils.createStream(context, None)
        val hashTag = "#" + args(0).toLowerCase()
        val tweets = twitterStream.map(status => status.getText())
            .filter(text => text.toLowerCase() contains hashTag)
        tweets.print()
        context.start()
        context.awaitTermination()
    }

}
