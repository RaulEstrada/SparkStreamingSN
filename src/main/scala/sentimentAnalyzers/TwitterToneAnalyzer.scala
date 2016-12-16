package main.scala.sentimentAnalyzers

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer
import scala.collection.JavaConversions._

object TwitterToneAnalyzer {

    val toneService = {
        val service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19)
        val username = sys.env.get("bluemix.toneanalyzer.username").get
        val passwd = sys.env.get("bluemix.toneanalyzer.password").get
        service.setUsernameAndPassword(username, passwd)
        service
    }

    def obtainSentiment(text: String): scala.collection.mutable.Map[String, scala.collection.mutable.Map[String, Double]] = {
        val tone = toneService.getTone(text, null).execute()
        val tones = tone.getDocumentTone().getTones()
        var result = scala.collection.mutable.Map[String, scala.collection.mutable.Map[String, Double]]()
        for (toneCategory <- tones) {
            val toneItem = scala.collection.mutable.HashMap[String, Double]()
            for (toneScore <- toneCategory.getTones()) {
                toneItem += (toneScore.getName() -> toneScore.getScore())
            }
            result += (toneCategory.getName() -> toneItem)
        }
        result
    }

}
