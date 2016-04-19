package com.edu.neu.coe.analysis
  
  import org.apache.log4j.{ Logger }
  import org.apache.spark.{ SparkConf, SparkContext }
  import org.apache.spark.sql.DataFrame
  import org.apache.spark.sql.SQLContext
  import com.edu.neu.coe.analysis.visualization.WordCloud
  import com.edu.neu.coe.analysis.compare.Compare
  import com.edu.neu.coe.analysis.compare.GeneratePdf

case class person(business: String, address: String)

object YelpDataAnalysis {
  //@transient lazy val logger = Logger.getLogger(getClass.getName)

  // start the main program
  def main(args: Array[String]): Unit = {
    WordCloud.outputSentimentGood("/Users/YuanHank/Desktop/final/goodSentiment") //Businessid ->  Bigrams
    WordCloud.outputSentimentBad("/Users/YuanHank/Desktop/final/badSentiment") //Businessid ->  Bigrams
    WordCloud.getWordCloud("/Users/YuanHank/Desktop/final/goodSentiment", "part-00000", "goodSentiments.png") //Wordcloud for +ve and -ve Bigrams
    WordCloud.getWordCloud("/Users/YuanHank/Desktop/final/badSentiment", "part-00000", "badSentiments.png") //Wordcloud for +ve and -ve Bigrams
////    WordCloud.outputSentimentBad("filePath+fileName")
    Compare.compare("/Users/YuanHank/Desktop/final/Compare")
    
    GeneratePdf.mapRDD
    println("sssssssssssssssssssssssssssssssssssss")
  }
}