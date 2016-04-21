package com.edu.neu.coe.analysis.visualization

import com.edu.neu.coe.analysis.clean.DataParsingUtil
import com.edu.neu.coe.analysis.model.Business
import wordcloud.PolarWordCloud
import wordcloud.PolarBlendMode
import wordcloud.CollisionMode
import wordcloud.bg.CircleBackground
import wordcloud.font.FontWeight
import wordcloud.font.scale.SqrtFontScalar
import wordcloud.nlp.FrequencyAnalyzer
import com.twitter.chill.Base64.InputStream
import java.util.Properties
import java.util.ArrayList
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.trees.Tree
import edu.stanford.nlp.util.CoreMap
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import scala.collection.mutable.WrappedArray
import com.edu.neu.coe.analysis.clean.DataCleansingUtil
//import wordcloud.font.scale.LinearFontScalar
//import wordcloud.bg.PixelBoundryBackground
//import wordcloud.bg.RectangleBackground
//import wordcloud.font.CloudFont
//import wordcloud.palette.ColorPalette
//import org.apache.commons.codec.binary.Base64
//import collection.JavaConverters._
//import java.net.URL
//import java.io.ByteArrayOutputStream
//import javax.imageio.ImageIO

object WordCloud {

  val nlpProps = {
    val props = new Properties()
    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment")
    props
  }

  val pipeline = new StanfordCoreNLP(nlpProps)

  def getWordCloud(filePath: String, fileName: String, outPutPicName: String) = {
    val frequencyAnalizer = new FrequencyAnalyzer()
    frequencyAnalizer.setWordFrequencesToReturn(750)
    val fn =  new java.io.File(filePath).listFiles().length
    val wordFrequencies = new ArrayList[wordcloud.WordFrequency]()
    for(i <- 0 to fn-2){
      val numOfZ = 5-i.toString().length()
      val realF = (fileName+i.toString()).substring(0,10)
      val file = new java.io.File(filePath + "/" + fileName)
      val stream = new java.io.FileInputStream(file)
      wordFrequencies.addAll(frequencyAnalizer.load(stream))
    }
    
    val wordCloud = new PolarWordCloud(600, 600, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR)
    wordCloud.setPadding(2)
    wordCloud.setBackground(new CircleBackground(300))
    wordCloud.setFontScalar(new SqrtFontScalar(10, 40))
    wordCloud.build(wordFrequencies)

    //val baos = new ByteArrayOutputStream()
    //ImageIO.write(wordCloud.getBufferedImage(), "png", baos)
    //val image = new String(Base64.encodeBase64(baos.toByteArray()))

    wordCloud.writeToFile(filePath + "/" + outPutPicName);
  }

  def getSentiment(strReview: String) = {
    val annotation = pipeline.process(strReview)

    val sentence = annotation.get(classOf[CoreAnnotations.SentencesAnnotation]).get(0)
    val tree = sentence.get(classOf[SentimentCoreAnnotations.AnnotatedTree])
    val sentiment = RNNCoreAnnotations.getPredictedClass(tree)

    sentiment match {
      case a if a >= 0 && a <= 1 => (strReview, "NEGATIVE")
      case a if a == 2           => (strReview, "NEUTRAL")
      case a if a > 2 && a <= 4  => (strReview, "POSITIVE")
      case a if a < 0 || a > 4   => (strReview, "NOT_UNDERSTOOD")
    }

  }

  //A lot of things, bigram, sentiment
  def getSentimentWithAll = {
    println("Begin to get Sentiments --- WordCloud def getSentimentWithAll")
    val bigrams = DataParsingUtil.getBigramWithAll
    println("Get all sentiments --- WordCloud def getSentimentWithAll")
    //To find bigrams is Positive or Negative
    println("Judging bigrams, Positive or Negative --- WordCloud def getSentimentWithAll")
    val res = for (bigram <- bigrams; bi <- bigram._2) yield (bigram._1, getSentiment(bi))
    println("Judgment finished --- WordCloud def getSentimentWithAll")
    res
  }
  def outputSentimentWithAll(fileName: String) = {
    println("group by businesses --- WordCloud def outputSentimentWithAll")
    //    val rd = getSentimentWithAll.groupBy(y => y._1).map(f => (f._1,f._2.map(z => z._2)))/(f._2.map(z => z._1),f._2.map(z => z._2)))
    val r = getSentimentWithAll.groupBy(y => y._1(0))
    println("after groupby, before mapping")
    val rd = r.map(f => (f._2.map(z => z._1), f._2.map(z => z._2)))
    println("Mapped P/N to every business review --- WordCloud def outputSentimentWithAll")
    //    .flatMap(f => (f._1:+f._2._1:+f._2._2))
    //    rd.foreach(f => println(f))
    //    rd.saveAsTextFile(fileName)
    rd
  }
  def outputSentiment(fileName: String) = {
    println("Geting all bigrams --- WorldCloud def outputSentiment")
    val c = getSentimentWithAll.map { y => val a = y._2._1.split(" "); a(0) + a(1) }
    //    c.foreach { x => println(x) }
    val sqlC = DataCleansingUtil.sqlContext
    //    import sqlC.implicits._
    //    c.toDF().write.format("com.databricks.spark.csv")
    //    .save(fileName)
    c.saveAsTextFile(fileName)
  }
  def outputSentimentGood(fileName: String) = {
    println("Geting positive bigrams --- WorldCloud def outputSentimentGood")
    val f = getSentimentWithAll.filter { x => x._2._2.equals("POSITIVE") }
    f.map { y => val a = y._2._1.split(" "); a(0) + a(1) }.saveAsTextFile(fileName)
  }
  def outputSentimentBad(fileName: String) = {
    println("Geting negative bigrams --- WorldCloud def outputSentimentBad")
    getSentimentWithAll.filter { x => x._2._2.equals("NEGATIVE") }.map { y => val a = y._2._1.split(" "); a(0) + a(1) }.saveAsTextFile(fileName)
  }
  def outputSentimentWithId(fileName: String) = {
    getSentimentWithAll.map { x => (x._1(0).toString() + ", " + x._2.toString()) }.saveAsTextFile(fileName)
  }

  def getRddBusiness = {
    val res = outputSentimentWithAll("").map { y =>
      val x = y._1.toSeq(0)
      val z = y._2
      new Business(x(0).toString(), x(1).toString(), x(2).asInstanceOf[WrappedArray[String]], x(3).toString(),
        x(4).toString(), x(5).toString(), x(6).asInstanceOf[Double], x(7).asInstanceOf[Double], z)
    }
    println("all Businesses prepared --- WordCloud def getRddBusiness")
    res
  }
}