package main.scala.sentimentAnalyzers

import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import java.util.Properties
import scala.collection.JavaConversions._

object StanfordNLP {
    val pipeline =  {
        val properties = new Properties()
        properties.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment")
        new StanfordCoreNLP(properties)
    }

    def obtainSentiment(text: String): Int = {
        val annotation = pipeline.process(text)
        var maxLength = 0
        var finalSentiment = -1
        for (sentence <- annotation.get(classOf[CoreAnnotations.SentencesAnnotation])){
            val sentimentTree = sentence.get(classOf[SentimentCoreAnnotations.AnnotatedTree])
            val sentiment = RNNCoreAnnotations.getPredictedClass(sentimentTree)
            if (sentence.toString().length() > maxLength) {
                maxLength = sentence.toString().length()
                finalSentiment = sentiment
            }
        }
        return finalSentiment
    }
}
