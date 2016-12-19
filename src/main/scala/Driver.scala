package main.scala

import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.twitter._
import twitter4j.Status
import main.scala.schema.Tweet
import main.scala.sentimentAnalyzers.StanfordNLP
import main.scala.sentimentAnalyzers.TwitterToneAnalyzer
import org.elasticsearch.spark._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import java.text.SimpleDateFormat

object Driver {
    val SECONDS = 20
    val EMOTION_MAP = "Emotion Tone"
    val LANG_TONE_MAP = "Language Tone"
    val SOCIAL_TONE_MAP = "Social Tone"

    def main(args: Array[String]) {
        if (args.length < 1) {
            println("SocialMediaStream Driver usage: <word>")
            System.exit(-1)
        }
        val conf = new SparkConf()
        // ElasticSearch-Hadoop configuration tso that ES-Had. creates and index
        // if its missing when writing data to Elastichsearch or fail
        conf.set("es.index.auto.create", "true")
        conf.set("es.resource", "twitter/docs")
        conf.set("es.nodes", "127.0.0.1")
        conf.set("es.port", "9200")
        conf.set("es.nodes.discovery", "true")
        conf.set("es.http.timeout", "5m")

        val context = new StreamingContext(conf, Seconds(SECONDS))
        val twitterStream = TwitterUtils.createStream(context, None)
        val tweetSentiments = getFilteredTweets(args(0), twitterStream)
            .map{tweet =>
                val sentiment = StanfordNLP.obtainSentiment(tweet.tweet)
                val toneMap = TwitterToneAnalyzer.obtainSentiment(tweet.tweet)
                tweet.languageToneMap = toneMap.get(LANG_TONE_MAP)
                tweet.emotionMap = toneMap.get(EMOTION_MAP)
                tweet.socialToneMap = toneMap.get(SOCIAL_TONE_MAP)
                tweet.stanfSentiment = Some(sentiment)
                tweet
            }
        tweetSentiments.print()
        // Send tweets to ElasticSearch
        tweetSentiments.foreachRDD { rdd =>
            rdd.saveToEs("twitter/docs")
        }

        context.start()
        context.awaitTermination()
    }

    def getFilteredTweets(term: String, twitterStream: DStream[Status]):
    DStream[Tweet] = {
        val hashTag = "#" + term.toLowerCase()
        twitterStream.map{status =>
            val username = status.getUser().getName()
            val date = status.getCreatedAt()
            val format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
            val formattedDateStr = format.format(date)
            val formattedDate = format.parse(formattedDateStr)
            val tweet = status.getText()
            val location = Option(status.getGeoLocation())
            val lat = location.map(loc => loc.getLatitude)
            val lng = location.map(loc => loc.getLongitude)
            new Tweet(username, formattedDate, tweet, lat, lng)
        }.filter(tweet => tweet.tweet.toLowerCase() contains hashTag)
    }

}
