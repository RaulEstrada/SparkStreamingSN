package main.scala

import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.twitter._
import twitter4j.Status
import main.scala.schema.Tweet

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
        val tweetSentiments = getFilteredTweets(args(0), twitterStream)
            .map{tweet =>
                val sentiment = SentimentUtil.obtainSentiment(tweet.tweet)
                (tweet, sentiment)
            }
        tweetSentiments.print()
        context.start()
        context.awaitTermination()
    }

    def getFilteredTweets(term: String, twitterStream: DStream[Status]):
    DStream[Tweet] = {
        val hashTag = "#" + term.toLowerCase()
        twitterStream.map{status =>
            val username = status.getUser().getName()
            val date = status.getCreatedAt()
            val tweet = status.getText()
            val location = Option(status.getGeoLocation())
            val lat = location.map(loc => loc.getLatitude)
            val lng = location.map(loc => loc.getLongitude)
            new Tweet(username, date, tweet, lat, lng)
        }.filter(tweet => tweet.tweet.toLowerCase() contains hashTag)
    }

}
