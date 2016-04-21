package com.edu.neu.coe.analysis
import java.io.FileInputStream;
import java.util.Properties
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

  val in = new FileInputStream("/Users/YuanHank/Documents/SourceCode/Eclipse/finalProjectYelp/projectConfig.properties");
  val properties = new Properties();
  properties.load(in)
  in.close();

  println("Getting properties from property file...")
  val getBusinessDataLocation = properties.getProperty("businessDataLocation")
  val getReviewDataLocation = properties.getProperty("reviewDataLocation")
  val getPosTaggerModelLocation = properties.getProperty("posTaggerModelLocation")

  val generatePdfPath = properties.getProperty("pdfGeneratePath")
  val generateFilePath = properties.getProperty("fileGeneratePath")
  println("Done, let's rock.")

  // start the main program
  def main(args: Array[String]): Unit = {
    println("=========================")
    val getBusinessDataLocation = this.getBusinessDataLocation
    val getReviewDataLocation = this.getReviewDataLocation
    val getPosTaggerModelLocation = this.getPosTaggerModelLocation

    val generatePdfPath = properties.getProperty("pdfGeneratePath")
    val generateFilePath = properties.getProperty("fileGeneratePath")

    WordCloud.outputSentimentGood("/Users/YuanHank/Desktop/s/goodSentiment") //Businessid ->  Bigrams
    WordCloud.outputSentimentBad("/Users/YuanHank/Desktop/s/badSentiment") //Businessid ->  Bigrams
    WordCloud.getWordCloud("/Users/YuanHank/Desktop/s/goodSentiment", "part-00000", "goodSentiments.png") //Wordcloud for +ve and -ve Bigrams
    WordCloud.getWordCloud("/Users/YuanHank/Desktop/s/badSentiment", "part-00000", "badSentiments.png") //Wordcloud for +ve and -ve Bigrams
    ////    WordCloud.outputSentimentBad("filePath+fileName")
    Compare.compare("/Users/YuanHank/Desktop/s/Compare")
    GeneratePdf.mapRDD("/Users/YuanHank/Desktop/s/Compare")
  }
}